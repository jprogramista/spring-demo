package com.example.webpage.service

import com.example.webpage.model.Role
import com.example.webpage.repository.RoleRepository
import org.springframework.stereotype.Service

interface RoleService {
    fun findAll() : List<Role>
    fun getRole(id: Long): Role?
}

@Service
class JpaRoleService(val roleRepository: RoleRepository) : RoleService {

    override fun getRole(id: Long): Role? {
        return roleRepository.findById(id).orElse(null)
    }

    override fun findAll() : List<Role> {
        return roleRepository.findAll()
    }

}