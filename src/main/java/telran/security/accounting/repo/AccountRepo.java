package telran.security.accounting.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.security.accounting.model.AccountDataDoc;

public interface AccountRepo extends MongoRepository<AccountDataDoc, String> {

}
