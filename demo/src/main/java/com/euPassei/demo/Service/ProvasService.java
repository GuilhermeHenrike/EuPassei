package com.euPassei.demo.Service;

import com.euPassei.demo.Entity.Provas;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Repository.ProvasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvasService {

    @Autowired
    private ProvasRepository provasRepository;



}
