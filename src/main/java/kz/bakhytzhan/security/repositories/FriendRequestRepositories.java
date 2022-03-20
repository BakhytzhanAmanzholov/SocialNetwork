package kz.bakhytzhan.security.repositories;

import kz.bakhytzhan.security.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepositories extends JpaRepository<FriendRequest, Long> {
}
