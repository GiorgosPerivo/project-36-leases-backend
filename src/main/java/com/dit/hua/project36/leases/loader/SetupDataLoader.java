package com.dit.hua.project36.leases.loader;

import com.dit.hua.project36.leases.entity.*;
import com.dit.hua.project36.leases.repository.PrivilegeRepository;
import com.dit.hua.project36.leases.repository.RoleRepository;
import com.dit.hua.project36.leases.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        // Create ROLE_ADMIN
        Privilege manageUsers
                    = createPrivilegeIfNotFound(EPrivilege.MANAGE_USERS);
        Privilege manageRoles
                = createPrivilegeIfNotFound(EPrivilege.MANAGE_ROLES);
        Privilege managePrivileges
                = createPrivilegeIfNotFound(EPrivilege.MANAGE_PRIVILEGES);


        Set<Privilege> adminPrivileges = new HashSet<>(Arrays.asList(
                manageUsers, manageRoles, managePrivileges));
        createRoleIfNotFound(ERole.ROLE_ADMIN, adminPrivileges);

        Privilege showLeases = createPrivilegeIfNotFound(EPrivilege.SHOW_LEASES);

        // Create ROLE_LEASER
        Privilege createLease
                = createPrivilegeIfNotFound(EPrivilege.CREATE_LEASE);
        Set<Privilege> leaserPrivileges = new HashSet<>(Arrays.asList(
                createLease, showLeases));
        createRoleIfNotFound(ERole.ROLE_LEASER, leaserPrivileges);

        // Create ROLE_TENANT
        Privilege acceptLease
                = createPrivilegeIfNotFound(EPrivilege.ACCEPT_LEASE);
        Privilege addDetailsToLease
                = createPrivilegeIfNotFound(EPrivilege.ADD_DETAILS_TO_LEASE);
        Set<Privilege> tenantPrivileges = new HashSet<>(Arrays.asList(
                acceptLease, addDetailsToLease, showLeases));
        createRoleIfNotFound(ERole.ROLE_TENANT, tenantPrivileges);


        // Create admin
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();

        Optional<User> foundAdmin = userRepository.findByUsername("admin");

        if (foundAdmin.isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@test.com");
            user.setPassword(passwordEncoder.encode("testadmin123"));
            user.setRole(adminRole);
            userRepository.save(user);
        }


        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(EPrivilege privilegeName) {
        Optional<Privilege> foundPrivilege = privilegeRepository.findByName(privilegeName);

        if (foundPrivilege.isEmpty()) {
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName);
            privilegeRepository.save(privilege);
            return privilege;

        }
        return foundPrivilege.get();
    }

    @Transactional
    Role createRoleIfNotFound(
            ERole roleName, Collection<Privilege> privileges) {
        Optional<Role> foundRole = roleRepository.findByName(roleName);

        if (foundRole.isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            role.setPrivileges((Set<Privilege>) privileges);
            roleRepository.save(role);
            return role;
        }

        return foundRole.get();
    }
}