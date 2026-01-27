package com.vendora.core.r2dbc;

import com.vendora.core.model.User;
import com.vendora.core.model.gateway.UserRepository;
import com.vendora.core.r2dbc.entity.UserEntity;
import com.vendora.core.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, Long, UserReactiveRepository> implements UserRepository {

  protected UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
      super(repository, mapper, userEntity -> mapper.map(userEntity, User.class));
  }

  @Override
  public Mono<User> findByEmailAndTenantId(String email, Long tenantId) {
    return this.repository.findByEmailAndTenantId(email, tenantId).map(this::toEntity);
  }

  @Override
  public Mono<Boolean> existsByEmailAndTenantId(String email, Long tenantId) {
    return this.repository.existsByEmailAndTenantId(email, tenantId);
  }

  @Override
  public Mono<User> findByUserIdAndTenantId(Long userId, Long tenantId) {
    return this.repository.findByUserIdAndTenantId(userId, tenantId).map(this::toEntity);
  }

  @Override
  public Mono<Boolean> existsByUserIdAndTenantId(Long userId, Long tenantId) {
    return this.repository.existsByUserIdAndTenantId(userId, tenantId);
  }

  @Override
  public Mono<User> findByUserId(Long userId) {
    return this.repository.findById(userId).map(this::toEntity);
  }
}

