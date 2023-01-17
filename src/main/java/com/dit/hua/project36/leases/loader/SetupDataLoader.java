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
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.name()).orElseThrow();

        Optional<User> foundAdmin = userRepository.findByUsername("admin");

        if (foundAdmin.isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@test.com");
            user.setPassword(passwordEncoder.encode("testadmin123"));
            user.setRole(adminRole);
            userRepository.save(user);
        }

        // Create leaser
        Role leaserRole = roleRepository.findByName(ERole.ROLE_LEASER.name()).orElseThrow();
        Optional<User> foundLeaser = userRepository.findByUsername("leaser");

        if (foundLeaser.isEmpty()) {
            User leaser = new User();
            leaser.setUsername("leaser");
            leaser.setEmail("leaser@test.com");
            leaser.setPassword(passwordEncoder.encode("testleaser123"));
            leaser.setRole(leaserRole);
            userRepository.save(leaser);
        }

        // Create tenant
        Role tenantRole = roleRepository.findByName(ERole.ROLE_TENANT.name()).orElseThrow();
        Optional<User> foundTenant = userRepository.findByUsername("tenant");

        if (foundTenant.isEmpty()) {
            User tenant = new User();
            tenant.setUsername("tenant");
            tenant.setEmail("tenant@test.com");
            tenant.setPassword(passwordEncoder.encode("testtenant123"));
            tenant.setRole(tenantRole);
            userRepository.save(tenant);
        }


        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(EPrivilege privilegeName) {
        Optional<Privilege> foundPrivilege = privilegeRepository.findByName(privilegeName.name());

        if (foundPrivilege.isEmpty()) {
            Privilege privilege = new Privilege();
            privilege.setName(privilegeName.name());
            privilegeRepository.save(privilege);
            return privilege;

        }
        return foundPrivilege.get();
    }

    @Transactional
    Role createRoleIfNotFound(
            ERole roleName, Collection<Privilege> privileges) {
        Optional<Role> foundRole = roleRepository.findByName(roleName.name());

        if (foundRole.isEmpty()) {
            Role role = new Role();
            role.setName(roleName.name());
            role.setPrivileges((Set<Privilege>) privileges);
            roleRepository.save(role);
            return role;
        }

        return foundRole.get();
    }
}