import 'package:employee_crud/models/online_employee.dart';
import 'package:employee_crud/screens/add_edit_online_employee_screen.dart';
import 'package:employee_crud/services/api_service.dart';
import 'package:flutter/material.dart';

class OnlineEmployeeScreen extends StatefulWidget {
  const OnlineEmployeeScreen({super.key});

  @override
  State<OnlineEmployeeScreen> createState() => _OnlineEmployeeScreenState();
}

class _OnlineEmployeeScreenState extends State<OnlineEmployeeScreen> {
  final ApiService _apiService = ApiService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Online Employee Management')),
      body: FutureBuilder<List<OnlineEmployee>>(
        future: _apiService.getAllEmployees(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
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
                leading: employee.image != null
                    ? Image.network(
                        employee.image!,
                        width: 50,
                        height: 50,
                        fit: BoxFit.cover,
                      )
                    : const Icon(Icons.person),
                title: Text(employee.name),
                subtitle: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(employee.email),
                    Text('Designation: ${employee.designation}'),
                  ],
                ),
                trailing: IconButton(
                  icon: const Icon(Icons.delete),
                  onPressed: () async {
                    try {
                      await _apiService.deleteEmployee(employee.id!);
                      setState(() {});
                    } catch (e) {
                      ScaffoldMessenger.of(
                        context,
                      ).showSnackBar(SnackBar(content: Text('Error: $e')));
                    }
                  },
                ),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) =>
                          AddEditOnlineEmployeeScreen(employee: employee),
                    ),
                  ).then((_) => setState(() {}));
                },
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => const AddEditOnlineEmployeeScreen(),
            ),
          ).then((_) => setState(() {}));
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
