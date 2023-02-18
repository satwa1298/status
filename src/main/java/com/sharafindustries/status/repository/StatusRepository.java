package com.sharafindustries.status.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharafindustries.status.model.Status;

public interface StatusRepository extends JpaRepository<Status, Integer>
{

}
