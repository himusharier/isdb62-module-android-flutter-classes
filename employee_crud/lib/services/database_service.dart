import 'package:employee_crud/models/student.dart';
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import 'package:employee_crud/models/employee.dart';

class DatabaseService {
  static Database? _database;

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDatabase();
    return _database!;
  }

  Future<Database> _initDatabase() async {
    final path = join(await getDatabasesPath(), 'employee.db');
    return await openDatabase(
      path,
      version: 1,
      onCreate: (db, version) async {
        await db.execute('''
          CREATE TABLE employees (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            designation TEXT NOT NULL
          )
        ''');
        await db.execute('''
          CREATE TABLE students (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            email TEXT NOT NULL,            
            gender TEXT NOT NULL,            
            country TEXT NOT NULL,            
            image_path TEXT,            
            hobbies TEXT
          )
        ''');
      },
    );
  }

  Future<void> insertEmployee(Employee employee) async {
    final db = await database;
    await db.insert(
      'employees',
      employee.toMap(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<List<Employee>> getEmployees() async {
    final db = await database;
    final maps = await db.query('employees');
    return List.generate(maps.length, (i) => Employee.fromMap(maps[i]));
  }


  Future<void> insertStudent(Student student) async {
    final db = await database;
    await db.insert(
        'students',
        student.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<List<Student>> getStudents() async {
    final db = await database;
    final maps = await db.query('students');
    return List.generate(maps.length, (i) => Student.fromMap(maps[i]));
  }

  Future<void> updateStudent(Student student) async {
    final db = await database;
    await db.update(
      'students',
      student.toMap(),
      where: 'id = ?',
      whereArgs: [student.id],
    );
  }

  Future<void> deleteStudent(int id) async {
    final db = await database;
    await db.delete(
      'students',
      where: 'id = ?',
      whereArgs: [id],
    );
  }

}