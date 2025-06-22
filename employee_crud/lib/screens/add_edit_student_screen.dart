import 'dart:math';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path_provider/path_provider.dart';
import 'package:employee_crud/models/student.dart';
import 'package:employee_crud/services/database_service.dart';
import 'dart:io';

class AddEditStudentScreen extends StatefulWidget {
  final Student? student;

  const AddEditStudentScreen({super.key, this.student});

  @override
  State<AddEditStudentScreen> createState() => _AddEditStudentScreenState();
}

class _AddEditStudentScreenState extends State<AddEditStudentScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  String _gender = 'Male';
  String _country = 'USA';
  bool _isCountrySelected = false;
  File? _image;
  final DatabaseService _databaseService = DatabaseService();
  final ImagePicker _picker = ImagePicker();
  final Map<String, bool> _hobbies = {
    'Reading': false,
    'Writing': false,
    'Gaming': false,
    'Sports': false,
  };


  @override
  void initState() {
    super.initState();
    if (widget.student != null) {
      _nameController.text = widget.student!.name;
      _emailController.text = widget.student!.email;
      _gender = widget.student!.gender;
      _country = widget.student!.country;
      _isCountrySelected = true;
      if (widget.student!.imagePath != null) {
        _image = File(widget.student!.imagePath!);
      }
      if (widget.student!.hobbies != null) {
        final hobbiesList = widget.student!.hobbies!.split(',');
        for (var hobby in hobbiesList) {
          if (_hobbies.containsKey(hobby)) {
            _hobbies[hobby] = true;
          }
        }
      }
    }
  }

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
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

  Future<String?> _saveImage(File image, int? id) async {
    final dir = await getApplicationDocumentsDirectory();
    final imagePath = '${dir.path}/student_${id ?? DateTime.now().millisecondsSinceEpoch}.jpg';
    await image.copy(imagePath);
    return imagePath;
  }

  void _saveStudent() async {
    if (_formKey.currentState!.validate() && _isCountrySelected) {
      String? imagePath = widget.student?.imagePath;
      if (_image != null) {
        imagePath = await _saveImage(_image!, widget.student?.id);
      }

      final selectedHobbies = _hobbies.entries
        .where((entry) => entry.value)
        .map((entry) => entry.key)
        .toList()
        .join(',');

      final student = Student(
        id: widget.student?.id,
        name: _nameController.text,
        email: _emailController.text,
        gender: _gender,
        country: _country,
        imagePath: imagePath,
        hobbies: selectedHobbies.isNotEmpty ? selectedHobbies : null,
      );

      if (widget.student == null) {
        await _databaseService.insertStudent(student);
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content:  Text('Student added successfully')),
        );
      } else {
        await _databaseService.updateStudent(student);
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Student updated successfully')),
        );
      }
      Navigator.pop(context);

    } else if (!_isCountrySelected) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select a country')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.student == null ? 'Add Student' : 'Edit Student'),
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
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter a name';
                    }
                    return null;
                  },
                ),
                TextFormField(
                  controller: _emailController,
                  decoration: const InputDecoration(labelText: 'Email'),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Please enter an email';
                    }
                    if (!RegExp(r'^[^@]+@[^@]+\.[^@]+').hasMatch(value)) {
                      return 'Please enter a valid email';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 20),
                const Text('Gender'),
                Row(
                  children: [
                    Radio<String>(
                      value: 'Male',
                      groupValue: _gender,
                      onChanged: (value) {
                        setState(() {
                          _gender = value!;
                        });
                      },
                    ),
                    const Text('Male'),
                    Radio<String>(
                      value: 'Female',
                      groupValue: _gender,
                      onChanged: (value) {
                        setState(() {
                          _gender = value!;
                        });
                      },
                    ),
                    const Text('Female'),
                  ],
                ),
                const SizedBox(height: 20),
                DropdownButtonFormField<String>(
                  value: _country,
                  decoration: const InputDecoration(labelText: 'Country'),
                  items: ['USA', 'Canada', 'UK', 'India'].map((country) {
                    return DropdownMenuItem<String>(
                      value: country,
                      child: Text(country),
                    );
                  }).toList(),
                  onChanged: (value) {
                    setState(() {
                      _country = value!;
                      _isCountrySelected = true;
                    });
                  },
                  validator: (value) {
                    if (!_isCountrySelected) {
                      return 'Please select a country';
                    }
                    return null;
                  },
                ),
                const SizedBox(height: 20),
                const Text('Hobbies'),
                ..._hobbies.keys.map((hobby) {
                  return CheckboxListTile(
                    title: Text(hobby),
                    value: _hobbies[hobby],
                    onChanged: (value) {
                      setState(() {
                        _hobbies[hobby] = value!;
                      });
                    },
                  );
                }).toList(),
                const SizedBox(height: 20),
                const Text('Profile Image'),
                _image != null
                  ? Image.file(_image!,
                  height: 100, width: 100, fit: BoxFit.cover)
                  : const Text('No image selected'),
                ElevatedButton(
                  onPressed: _pickImage,
                  child: const Text('Pick Image'),
                ),
                const SizedBox(height: 20),
                ElevatedButton(
                  onPressed: _saveStudent,
                  child: Text(widget.student == null
                    ? 'Add Student'
                    : 'Update Student'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
