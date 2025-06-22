import 'package:flutter/material.dart';
import 'package:employee_crud/services/database_service.dart';
import 'package:employee_crud/screens/add_edit_student_screen.dart';
import 'package:employee_crud/models/student.dart';
import 'dart:io';

class StudentScreen extends StatefulWidget {
  const StudentScreen({super.key});

  @override
  State<StudentScreen> createState() => _StudentScreenState();
}

class _StudentScreenState extends State<StudentScreen> {
  final DatabaseService _databaseService = DatabaseService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Student Management'),
      ),
      body: FutureBuilder<List<Student>>(
        future: _databaseService.getStudents(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('No students found'));
          }
          final students = snapshot.data!;
          return ListView.builder(
            itemCount: students.length,
            itemBuilder: (context, index) {
              final student = students[index];
              return ListTile(
                leading: student.imagePath != null
                  ? Image.file(File(student.imagePath!),
                  width: 50, height: 50, fit: BoxFit.cover)
                  : const Icon(Icons.person),
                title: Text(student.name),
                subtitle: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(student.email),
                    if (student.hobbies != null)
                      Text('Hobbies: ${student.hobbies}'),
                  ],
                ),
                trailing: IconButton(
                  icon: const Icon(Icons.delete),
                  onPressed: () async {
                    await _databaseService.deleteStudent(student.id!);
                    setState(() {});
                  },
                ),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => AddEditStudentScreen(),
            ),
          ).then((_) => setState(() {}));
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
