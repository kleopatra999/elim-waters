package com.church.elim.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.church.elim.domain.Children;
import com.church.elim.repository.ChildrenRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChildrenService {
	@Autowired 
	ChildrenRepository childrenRepo;
	@PersistenceContext
	public EntityManager entityManager;

	public boolean exists(Children children){
		if(children!=null && children.getParent()!=null && children.getChild()!=null){
			List<Children> crtChildren = childrenRepo.findByParentIdAndChildId(
					children.getParent().getId(), 
					children.getChild().getId());
			if(crtChildren.size()>0){
				return true;
			}
		}
		
		return false;
	}

    @Transactional
    public Children save(Children children) {
        return childrenRepo.saveAndFlush(children);
    }
}
