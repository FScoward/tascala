package modules

import com.google.inject.AbstractModule
import domain.model.task.TaskRepository
import domain.repository.auth.CredentialRepository
import domain.repository.user.UserRepository
import repository.auth.CredentialRepositoryImpl
import repository.task.TaskRepositoryImpl
import repository.user.UserRepositoryImpl

class RepositoryModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[TaskRepository]).to(classOf[TaskRepositoryImpl])
    bind(classOf[CredentialRepository]).to(classOf[CredentialRepositoryImpl])
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl])
  }
}
