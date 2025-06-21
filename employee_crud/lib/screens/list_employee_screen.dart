import 'package:flutter/material.dart';
import 'package:employee_crud/models/employee.dart';
import 'package:employee_crud/models/database_service.dart';

class ListEmployeeScreen extends StatefulWidget {
  const ListEmployeeScreen({super.key});

  @override
  State<ListEmployeeScreen> createState() => _ListEmployeeScreenState();
}

class _ListEmployeeScreenState extends State<ListEmployeeScreen> {
  final DatabaseService _databaseService = DatabaseService();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<Employee>>(
      future: _databaseService.getEmployees(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator());
        }
        if (!snapshot.hasData || snapshot.data!.isEmpty) {
          return const Center(child: Text('No employees found'));
        }
        final employees = snapshot.data!;
        return ListView.builder(
          itemCount: employees.length,
          itemBuilder: (context, index) {
            final employee = employees[index];
            return ListTile(
              title: Text(employee.name),
              subtitle: Text(employee.designation),
            );
          },
        );
      },
    );
  }
}
