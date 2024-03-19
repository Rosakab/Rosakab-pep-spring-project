

package com.example.repository;
import java.util.List;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Message m WHERE m.id = ?1")
    int deleteMessageById(Integer messageId);

    @Query("SELECT m FROM Message m WHERE m.posted_by = ?1")
    List<Message> findAllByPosted_by(Integer posted_by);
}

