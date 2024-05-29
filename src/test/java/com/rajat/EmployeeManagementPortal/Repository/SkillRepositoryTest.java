package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SkillRepositoryTest {

  @Autowired
  private SkillRepository skillRepository;

  @Test
  void testSaveSkill() {
    Skill skill = Skill.builder()
      .skillName("Python")
      .build();

    Skill savedSkill = skillRepository.save(skill);

    assertThat(savedSkill).isNotNull();
    assertThat(savedSkill.getSkillName()).isEqualTo(skill.getSkillName());
  }

  @Test
  void testFindBySkillName() {
    Skill skill = Skill.builder()
      .skillName("Python")
      .build();
    skillRepository.save(skill);

    String skillName = "Python";
    Skill foundSkill = skillRepository.findBySkillName(skillName);

    assertThat(foundSkill).isNotNull();
    assertThat(foundSkill.getSkillName()).isEqualTo(skillName);
  }

  @Test
  void testFindAllSkills() {
    Skill skill1 = Skill.builder()
      .skillName("Python")
      .build();

    Skill skill2 = Skill.builder()
      .skillName("Java")
      .build();

    skillRepository.save(skill1);
    skillRepository.save(skill2);

    List<Skill> skills = skillRepository.findAll();

    assertThat(skills).isNotNull();
    assertThat(skills.size()).isEqualTo(2);
  }
}