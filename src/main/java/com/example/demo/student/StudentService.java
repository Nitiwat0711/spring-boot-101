package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return  studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);

    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);

        if (!exists) {
            throw  new IllegalStateException("student with id " + studentId +
                    " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Student student) {
        Optional<Student> updateStudent = studentRepository.findById(student.getId());

        if (updateStudent.isEmpty()) {
            throw  new IllegalStateException("student with id " + student.getId() +
                    " does not exists");
        }

        if (updateStudent.get().getName().equals(student.getName()) || updateStudent.get().getEmail().equals(student.getEmail())) {
            throw  new IllegalStateException("name or email not change.");
        }

        updateStudent.get().setName(student.getName());
        updateStudent.get().setEmail(student.getEmail());
        studentRepository.save(updateStudent.get());
    }
}
