package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RequestRepositoryTest {

  @Autowired
  private RequestRepository requestRepository;

  @Test
  void testSaveRequest() {
    Request request = Request.builder()
      .skill("Java")
      .employeesRequired(3)
      .build();

    Request savedRequest = requestRepository.save(request);

    assertThat(savedRequest).isNotNull();
    assertThat(savedRequest.getEmployeesRequired()).isEqualTo(request.getEmployeesRequired());
  }

  @Test
  void testFindAllRequest() {

    Request request1 = Request.builder()
      .skill("Java")
      .employeesRequired(3)
      .build();

    Request request2 = Request.builder()
      .skill("NodeJS")
      .employeesRequired(2)
      .build();

    requestRepository.save(request1);
    requestRepository.save(request2);

    List<Request> requests = requestRepository.findAll();

    assertThat(requests).isNotNull();
    assertThat(requests.size()).isEqualTo(2);
  }

  @Test
  void testFindByRequestedBy() {
    Request request = Request.builder()
      .skill("Java")
      .employeesRequired(3)
      .requestedBy("John")
      .build();

    requestRepository.save(request);

    List<Request> requests = new ArrayList<>();
    requests.add(request);

    List<Request> response = requestRepository.findByRequestedBy("John");

    assertThat(response).isNotNull();
    assertThat(response.size()).isEqualTo(1);
    assertThat(response.get(0).getSkill()).isEqualTo(request.getSkill());
  }

}
