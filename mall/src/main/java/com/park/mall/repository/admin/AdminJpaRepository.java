package com.park.mall.repository.admin;

import com.park.mall.domain.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminJpaRepository extends JpaRepository<Admin, String> {

}
