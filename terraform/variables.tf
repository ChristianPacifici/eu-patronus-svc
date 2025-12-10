variable "aws_region" {
  description = "AWS Region"
  type        = string
  default     = "eu-central-1"
}

variable "cluster_name" {
  description = "EKS Cluster Name"
  type        = string
  default     = "patronus-eks"
}

variable "db_password" {
  description = "Postgres Password"
  type        = string
  sensitive   = true
}

variable "db_username" {
  description = "Database username"
  type        = string
  default     = "postgres"
}

variable "app_image_tag" {
  description = "Docker image tag"
  type        = string
  default     = "latest"
}

variable "namespace" {
  description = "Namespace Kubernetes"
  type        = string
  default     = "patronus"
}