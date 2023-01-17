package com.dit.hua.project36.leases.controller;

import com.dit.hua.project36.leases.config.JwtUtils;
import com.dit.hua.project36.leases.payload.request.LoginRequest;
import com.dit.hua.project36.leases.repository.PrivilegeRepository;
import com.dit.hua.project36.leases.repository.RoleRepository;
import com.dit.hua.project36.leases.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MvcController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping(value = "/login")
    public ModelAndView loginView() {
        return new ModelAndView("login", "user", new LoginRequest());
    }

    @GetMapping(value = "/users")
    public ModelAndView usersView(Authentication authentication, ModelMap model) {

        model.put("userList", userRepository.findAll());
        model.put("jwtToken", jwtUtils.generateJwtToken(authentication));
        return new ModelAndView("users", model);
    }

    @GetMapping(value = "/createUser")
    public ModelAndView createUserView(Authentication authentication, ModelMap model) {
        model.put("roleList", roleRepository.findAll());
        model.put("jwtToken", jwtUtils.generateJwtToken(authentication));

        return new ModelAndView("createUser", model);
    }

    @GetMapping(value = "/privileges")
    public ModelAndView privilegesView(Authentication authentication, ModelMap model) {
        model.put("privilegeList", privilegeRepository.findAll());
        model.put("jwtToken", jwtUtils.generateJwtToken(authentication));

        return new ModelAndView("privileges", model);
    }

    @GetMapping(value = "/createPrivilege")
    public ModelAndView createPrivilegeView(Authentication authentication, ModelMap model) {
        model.put("jwtToken", jwtUtils.generateJwtToken(authentication));

        return new ModelAndView("createPrivilege", model);
    }

    @GetMapping(value = "/roles")
    public ModelAndView rolesView(ModelMap model) {

        model.put("roleList", roleRepository.findAll());
        return new ModelAndView("roles", model);
    }

    @GetMapping(value = "/createRole")
    public ModelAndView createRoleView(Authentication authentication, ModelMap model) {
        model.put("jwtToken", jwtUtils.generateJwtToken(authentication));
        model.put("privilegeList", privilegeRepository.findAll());

        return new ModelAndView("createRole", model);
    }


}
