class Employee {
  final int? id;
  final String name;
  final String designation;

  Employee({this.id, required this.name, required this.designation});

  Map<String, dynamic> toMap() {
    return {'id': id, 'name': name, 'designation': designation};
  }

  factory Employee.fromMap(Map<String, dynamic> map) {
    return Employee(
      id: map['id'],
      name: map['name'],
      designation: map['designation'],
    );
  }

}