package com.example.webpage.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.util.ProxyUtils
import java.time.Instant
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity<T : java.io.Serializable> /*: Persistable<T>*/ {

    companion object {
        private val serialVersionUID = -5551308932980869754L
    }

    @Id
    @GeneratedValue
    /*private*/ var id: T? = null

    @field:CreationTimestamp
    open var createdAt: Instant? = null

    @field:UpdateTimestamp
    open val updatedAt: Instant? = null

    /*override fun getId(): T? {
        return id
    }

    override fun isNew(): Boolean {
        return id == null
    }*/

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as BaseEntity<*>

        //return if (null == this.getId()) false else this.getId() == other.getId()
        return if (null == this.id) false else this.id == other.id
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

}