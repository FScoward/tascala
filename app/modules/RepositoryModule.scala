package modules

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import domain.model.task.{DoingRepository, DoneRepository, IceboxRepository, TaskRepository}
import domain.repository.auth.CredentialRepository
import domain.repository.user.UserRepository
import repository.auth.CredentialRepositoryImpl
import repository.task.{DoingRepositoryImpl, DoneRepositoryImpl, IceboxRepositoryImpl, TaskRepositoryImpl}
import repository.user.UserRepositoryImpl

class RepositoryModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[TaskRepository]).to(classOf[TaskRepositoryImpl])
    bind(classOf[IceboxRepository]).to(classOf[IceboxRepositoryImpl])
    bind(classOf[CredentialRepository]).to(classOf[CredentialRepositoryImpl])
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl])
    bind(classOf[DoneRepository]).to(classOf[DoneRepositoryImpl])
    bind(classOf[DoingRepository]).to(classOf[DoingRepositoryImpl])
  }
}
