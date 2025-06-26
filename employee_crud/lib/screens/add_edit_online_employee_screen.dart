
import 'dart:io';

import 'package:employee_crud/models/online_employee.dart';
import 'package:employee_crud/services/api_service.dart';
import 'package:flutter/cupertino.dart';
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
  Widget build(BuildContext context) {
    return const Placeholder();
  }

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

}
