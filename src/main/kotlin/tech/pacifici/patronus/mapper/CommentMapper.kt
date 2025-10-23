package tech.pacifici.patronus.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component
import tech.pacifici.patronus.model.CommentRequest
import tech.pacifici.patronus.model.CommentResponse
import tech.pacifici.patronus.model.tables.records.CommentsRecord

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
@Component
interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toCommentRecord(request: CommentRequest): CommentsRecord

    fun toCommentResponse(record: CommentsRecord): CommentResponse
}
