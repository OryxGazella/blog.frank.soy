package soy.frank.blog.models

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "posts")
data class Post(
        @Id
        val id: Long = -1,
        val title: String = "",
        val renderedBody: String = "",
        @Convert(converter = LocalDateTimePersistenceConverter::class)
        val createdAt: LocalDateTime = LocalDateTime.MIN)


@Converter(autoApply = true)
class LocalDateTimePersistenceConverter : AttributeConverter<LocalDateTime, Timestamp> {
    override fun convertToDatabaseColumn(attribute: LocalDateTime?): Timestamp? {
        return Timestamp.valueOf(attribute)
    }

    override fun convertToEntityAttribute(dbData: Timestamp?): LocalDateTime? {
        return dbData?.toLocalDateTime()
    }
}