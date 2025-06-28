import 'dart:convert';
import 'dart:io';

import 'package:employee_crud/models/online_employee.dart';
import 'package:http/http.dart' as  http;
import 'package:path/path.dart';

class ApiService {
  // static const String baseUrl = 'http://10.0.2.2:8081';
  static const String baseUrl = 'http://192.168.0.79:8081';

  Future<List<OnlineEmployee>> getAllEmployees() async {
    final response = await http.post(Uri.parse('$baseUrl/employee'));
    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => OnlineEmployee.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load employee');
    }
  }

  Future<OnlineEmployee> getEmployeeById(int id) async{
    final response = await http.get(Uri.parse('$baseUrl/employee/$id'));
    if(response.statusCode == 200){
      return OnlineEmployee.fromJson(jsonDecode(response.body));
    }else{
      throw Exception('Failed to load employye');
    }
  }

  Future<OnlineEmployee> saveEmployee(OnlineEmployee employee) async {
    final response = await http.post(
      Uri.parse('$baseUrl/employee'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(employee.toJson()),
    );
    if (response.statusCode == 200) {
      return OnlineEmployee.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to save employee');
    }
  }

  Future<OnlineEmployee> updateEmployee(int id, OnlineEmployee employee) async {
    final response = await http.put(
      Uri.parse('$baseUrl/employee/$id'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(employee.toJson()),
    );
    if (response.statusCode == 200) {
      return OnlineEmployee.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to update employee');
    }
  }

  Future<void> deleteEmployee(int id) async {
    final response = await http.delete(Uri.parse('$baseUrl/employee/$id'));
    if (response.statusCode != 200) {
      throw Exception('Failed to delete employee');
    }
  }

  Future<String> uploadImage(int employeeId, File image) async {
    var request = http.MultipartRequest(
      'POST',
      Uri.parse('$baseUrl/employee/$employeeId/upload'),
    );
    request.files.add(
      await http.MultipartFile.fromPath(
          'file',
          image.path,
          filename: basename(image.path),
      ),
    );
    final response = await request.send();
    if (response.statusCode == 200) {
      final responseBody = await response.stream.bytesToString();
      final jsonResponse = jsonDecode(responseBody);
      return jsonResponse['url'];
    } else {
      throw Exception('Failed to update image');
    }
  }
}