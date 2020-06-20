package net.gazpromkitchen.server.repository;

import net.gazpromkitchen.server.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
