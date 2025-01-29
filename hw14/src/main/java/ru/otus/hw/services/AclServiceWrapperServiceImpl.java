package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    @Override
    public void createPermission(Object object) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(object);

        final Sid editor = new GrantedAuthoritySid("ROLE_EDITOR");

        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.setOwner(owner);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, editor, true);
        mutableAclService.updateAcl(acl);
    }
}
