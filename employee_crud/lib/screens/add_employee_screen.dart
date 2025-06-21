import 'package:flutter/material.dart';
import 'package:employee_crud/models/employee.dart';
import 'package:employee_crud/models/database_service.dart';

class AddEmployeeScreen extends StatefulWidget {
  const AddEmployeeScreen({super.key});

  @override
  State<AddEmployeeScreen> createState() => _AddEmployeeScreenState();
}

class _AddEmployeeScreenState extends State<AddEmployeeScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _designationController = TextEditingController();
  final _databaseService = DatabaseService();


  @override
  void dispose() {
    _nameController.dispose();
    _designationController.dispose();
    super.dispose();
  }

  void _saveEmployee() async {
    if (_formKey.currentState!.validate()) {
      final employee = Employee(
        name: _nameController.text,
        designation: _designationController.text,
      );
      await _databaseService.insertEmployee(employee);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Employee added successfully')),
      );
      _nameController.clear();
      _designationController.clear();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Form(
        key: _formKey,
        child: Column(
          children: [
            TextFormField(
              controller: _nameController,
              decoration: const InputDecoration(labelText: 'Name'),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please enter a name';
                }
                return null;
              },
            ),
            TextFormField(
              controller: _designationController,
              decoration: const InputDecoration(labelText: 'Designation'),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please enter a designation';
                }
                return null;
              },
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _saveEmployee,
              child: const Text('Add Employee'),
            ),
          ],
        ),
      ),
    );
  }
}
