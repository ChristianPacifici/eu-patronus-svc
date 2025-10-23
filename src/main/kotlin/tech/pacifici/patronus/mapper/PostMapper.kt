package tech.pacifici.patronus.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component
import tech.pacifici.patronus.model.PostRequest
import tech.pacifici.patronus.model.PostResponse
import tech.pacifici.patronus.model.tables.records.PostsRecord

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
@Component
interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toPostsRecord(request: PostRequest): PostsRecord

    fun toPostResponse(record: PostsRecord): PostResponse

    fun toPostResponseList(records: List<PostsRecord>): List<PostResponse>
}
