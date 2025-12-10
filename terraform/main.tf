terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.0"
    }
  }
}

# AWS Provider
provider "aws" {
  region = var.aws_region
}

# Kubernetes Provider (configured after EKS cluster creation)
provider "kubernetes" {
  host                   = module.eks.cluster_endpoint
  cluster_ca_certificate = base64decode(module.eks.cluster_certificate_authority_data)
  token                  = data.aws_eks_cluster_auth.cluster.token
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}

provider "helm" {
  kubernetes {
    host                   = module.eks.cluster_endpoint
    cluster_ca_certificate = base64decode(module.eks.cluster_certificate_authority_data)
    token                  = data.aws_eks_cluster_auth.cluster.token
  }
}

# Variables
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-west-2"
}

variable "cluster_name" {
  description = "Name of the EKS cluster"
  type        = string
  default     = "patronus-eks-cluster"
}

variable "cluster_version" {
  description = "Kubernetes version"
  type        = string
  default     = "1.29"
}

variable "instance_type" {
  description = "EC2 instance type for node group"
  type        = string
  default     = "t3.medium"
}

variable "desired_capacity" {
  description = "Desired number of nodes in the node group"
  type        = number
  default     = 2
}

variable "db_username" {
  description = "RDS PostgreSQL username"
  type        = string
  default     = "postgres"
}

variable "db_password" {
  description = "RDS PostgreSQL password"
  type        = string
  sensitive   = true
}

# Locals
locals {
  namespace = "patronus"
}

# VPC Module (for EKS)
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"

  name = "patronus-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["${var.aws_region}a", "${var.aws_region}b"]
  private_subnets = ["#CIDR", "#CIDR"]
  public_subnets  = ["#CIDR", "#CIDR"]

  enable_nat_gateway = true
  enable_vpn_gateway = false

  tags = {
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  }
}

# EKS Module
module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 20.0"

  cluster_name    = var.cluster_name
  cluster_version = var.cluster_version
  vpc_id          = module.vpc.vpc_id
  subnet_ids      = module.vpc.private_subnets

  eks_managed_node_groups = {
    default = {
      min_size     = 1
      max_size     = 3
      desired_size = var.desired_capacity

      instance_types = [var.instance_type]
      capacity_type  = "ON_DEMAND"
    }
  }

  tags = {
    Environment = "dev"
  }
}

# RDS PostgreSQL Instance
resource "aws_db_subnet_group" "patronus" {
  name       = "patronus-subnet-group"
  subnet_ids = module.vpc.private_subnets

  tags = {
    Name = "patronus-db-subnet-group"
  }
}

resource "aws_db_instance" "patronus_db" {
  identifier              = "eu-patronus-db"
  engine                  = "postgres"
  engine_version          = "14"
  instance_class          = "db.t3.micro"
  allocated_storage       = 20
  storage_type            = "gp2"
  db_name                 = "eu_patronus_db"
  username                = var.db_username
  password                = var.db_password
  db_subnet_group_name    = aws_db_subnet_group.patronus.name
  vpc_security_group_ids  = [aws_security_group.rds.id]
  skip_final_snapshot     = true
  publicly_accessible     = false

  tags = {
    Name = "patronus-db"
  }
}

# Security Group for RDS
resource "aws_security_group" "rds" {
  name_prefix = "patronus-rds-"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [module.eks.cluster_primary_security_group_id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "patronus-rds-sg"
  }
}

# Kubernetes Namespace
resource "kubernetes_namespace" "patronus" {
  metadata {
    name = local.namespace
  }
}

# Secret for DB Credentials
resource "kubernetes_secret" "db_secret" {
  metadata {
    name      = "db-secret"
    namespace = local.namespace
  }

  data = {
    username = var.db_username
    password = var.db_password
  }

  type = "Opaque"
}

# ECR Repository for App Image
resource "aws_ecr_repository" "patronus" {
  name                 = "patronus-social-network"
  image_tag_mutability = "MUTABLE"

  tags = {
    Name = "patronus-ecr"
  }
}

# Kubernetes Deployment
resource "kubernetes_deployment" "patronus_app" {
  metadata {
    name      = "patronus-app"
    namespace = local.namespace
    labels = {
      app = "patronus"
    }
  }

  spec {
    replicas = 2

    selector {
      match_labels = {
        app = "patronus"
      }
    }

    template {
      metadata {
        labels = {
          app = "patronus"
        }
      }

      spec {
        container {
          name  = "patronus"
          image = "${aws_ecr_repository.patronus.repository_url}:latest"

          port {
            container_port = 8080
          }

          env {
            name = "SPRING_DATASOURCE_URL"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_secret.metadata[0].name
                key  = "url"  # User to set full JDBC URL via secret or configmap
              }
            }
          }

          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_secret.metadata[0].name
                key  = "username"
              }
            }
          }

          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_secret.metadata[0].name
                key  = "password"
              }
            }
          }

          resources {
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
            requests = {
              cpu    = "250m"
              memory = "256Mi"
            }
          }
        }
      }
    }
  }

  depends_on = [module.eks.eks_managed_node_groups]
}

# Note: Update the SPRING_DATASOURCE_URL in secret to: jdbc:postgresql://${aws_db_instance.patronus_db.endpoint}/${aws_db_instance.patronus_db.db_name}

# Kubernetes Service (LoadBalancer)
resource "kubernetes_service" "patronus_service" {
  metadata {
    name      = "patronus-service"
    namespace = local.namespace
  }

  spec {
    selector = {
      app = "patronus"
    }

    port {
      port        = 80
      target_port = 8080
    }

    type = "LoadBalancer"
  }

  depends_on = [kubernetes_deployment.patronus_app]
}

# Outputs
output "cluster_endpoint" {
  description = "Endpoint for EKS control plane"
  value       = module.eks.cluster_endpoint
}

output "cluster_name" {
  description = "Kubernetes cluster name"
  value       = module.eks.cluster_id
}

output "load_balancer_ingress" {
  description = "Load Balancer Ingress for the service"
  value       = kubernetes_service.patronus_service.status[0].load_balancer[0].ingress[0].hostname
}

output "db_endpoint" {
  description = "RDS PostgreSQL endpoint"
  value       = aws_db_instance.patronus_db.endpoint
}

output "ecr_repository_url" {
  description = "ECR repository URL for pushing the image"
  value       = aws_ecr_repository.patronus.repository_url
}