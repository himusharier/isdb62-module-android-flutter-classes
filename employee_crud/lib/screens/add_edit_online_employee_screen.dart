import 'dart:io';

import 'package:employee_crud/models/online_employee.dart';
import 'package:employee_crud/services/api_service.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

class AddEditOnlineEmployeeScreen extends StatefulWidget {
  final OnlineEmployee? employee;

  const AddEditOnlineEmployeeScreen({super.key, this.employee});

  @override
  State<AddEditOnlineEmployeeScreen> createState() => _AddEditOnlineEmployeeScreenState();
}

class _AddEditOnlineEmployeeScreenState extends State<AddEditOnlineEmployeeScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _designationController = TextEditingController();
  final _ageController = TextEditingController();
  final _addressController = TextEditingController();
  final _dobController = TextEditingController();
  final _salaryController = TextEditingController();
  File? _image;
  String? _imageUrl;
  final ApiService _apiService = ApiService();
  final ImagePicker _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    if (widget.employee != null) {
      _nameController.text = widget.employee!.name;
      _emailController.text = widget.employee!.email;
      _designationController.text = widget.employee!.designation;
      _ageController.text = widget.employee!.age.toString();
      _addressController.text = widget.employee!.address;
      _dobController.text = widget.employee!.dob;
      _salaryController.text = widget.employee!.salary.toString();
      _imageUrl = widget.employee!.image;
    }
  }

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _designationController.dispose();
    _ageController.dispose();
    _addressController.dispose();
    _dobController.dispose();
    _salaryController.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final pickedFile = await _picker.pickImage(source: ImageSource.gallery);
    if (pickedFile != null) {
      setState(() {
        _image = File(pickedFile.path);
      });
    }
  }

  Future<void> _saveEmployee() async {
    if (_formKey.currentState!.validate()) {
      final employee = OnlineEmployee(
        id: widget.employee?.id,
        name: _nameController.text,
        email: _emailController.text,
        designation: _designationController.text,
        age: int.parse(_ageController.text),
        address: _addressController.text,
        dob: _dobController.text,
        salary: double.parse(_salaryController.text),
        image: _imageUrl,
      );

      try {
        OnlineEmployee savedEmployee;
        if (widget.employee == null) {
          savedEmployee = await _apiService.saveEmployee(employee);
        } else {
          savedEmployee = await _apiService.updateEmployee(
              widget.employee!.id!,
              employee,
          );
        }

        if (_image != null) {
          final imageUrl = await _apiService.uploadImage(
            savedEmployee.id!,
            _image!,
          );
          final updatedEmployee = OnlineEmployee(
            id: savedEmployee.id,
            name: savedEmployee.name,
            email: savedEmployee.email,
            designation: savedEmployee.designation,
            age: savedEmployee.age,
            address: savedEmployee.address,
            dob: savedEmployee.dob,
            salary: savedEmployee.salary,
            image: imageUrl,
          );
          await _apiService.updateEmployee(savedEmployee.id!, updatedEmployee);
        }

        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(
                widget.employee == null ? 'Employee added' : 'Employee updated',
              ),
            ),
        );
        Navigator.pop(context);
      } catch (e) {
        ScaffoldMessenger.of(
            context,
        ).showSnackBar(SnackBar(content: Text('Error: $e')));
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.employee == null ? 'Add Employee' : 'Edit Employee'),
      ),
      body: Padding(
          padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
            child: SingleChildScrollView(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(labelText: 'Name'),
                    validator: (value) => value!.isEmpty ? 'Enter a name' : null,
                  ),
                  TextFormField(
                    controller: _emailController,
                    decoration: const InputDecoration(labelText: 'Email'),
                    validator: (value) {
                      if (value!.isEmpty) return 'Enter a email';
                      if (!RegExp(r'^[^@]+@[^@]+\.[^@]+').hasMatch(value)) {
                        return 'Enter a valid email';
                      }
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _designationController,
                    decoration: const InputDecoration(labelText: 'Designation'),
                    validator: (valid) =>
                        valid!.isEmpty ? 'Enter a designation' : null,
                  ),
                  TextFormField(
                    controller: _ageController,
                    decoration: const InputDecoration(labelText: 'Age'),
                    keyboardType: TextInputType.number,
                    validator: (value) {
                      if (value!.isEmpty) return 'Enter an age';
                      if (int.tryParse(value) == null)
                        return 'Enter a valid number';
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _addressController,
                    decoration: const InputDecoration(labelText: 'Address'),
                    validator: (value) =>
                        value!.isEmpty ? 'Enter an address' : null,
                  ),
                  TextFormField(
                    controller: _dobController,
                    decoration: const InputDecoration(
                        labelText: 'Date of Birth (yyyy-MM-dd)',
                    ),
                    validator: (value) {
                      if (value!.isEmpty) return 'Enter a date';
                      if (!RegExp(r'^\d{4}-\d{2}-\d{2}$').hasMatch(value)) {
                        return 'Enter date as yyyy-MM-dd';
                      }
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _salaryController,
                    decoration: const InputDecoration(labelText: 'Salary'),
                    keyboardType: TextInputType.number,
                    validator: (value) {
                      if (value!.isEmpty) return 'Enter a salary';
                      if (double.tryParse(value) == null)
                        return 'Enter a valid number';
                      return null;
                    },
                  ),
                  const SizedBox(height: 20),
                  const Text('Profile Image'),
                  _image != null
                      ? Image.file(_image!, height: 100, width: 100, fit: BoxFit.cover)
                      : _imageUrl != null
                      ? Image.network(_imageUrl!, height: 100, width: 100, fit: BoxFit.cover)
                      : const Text('No image selected'),
                  ElevatedButton(
                      onPressed: _pickImage,
                      child: const Text('Pick Image'),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
                      onPressed: _saveEmployee,
                      child: Text(
                        widget.employee == null
                            ? 'Add Employee'
                            : 'Update Employee'
                      ),
                  ),
                ],
              ),
            ),
        ),
      ),
    );
  }

}
