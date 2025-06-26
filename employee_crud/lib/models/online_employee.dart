class OnlineEmployee {
  final int? id;
  final String name;
  final String email;
  final String designation;
  final int age;
  final String address;
  final String dob;
  final double salary;
  final String? image;

  OnlineEmployee({
    this.id,
    required this.name,
    required this.email,
    required this.designation,
    required this.age,
    required this.address,
    required this.dob,
    required this.salary,
    this.image,
});

  factory OnlineEmployee.fromJson(Map<String, dynamic> json) {
    return OnlineEmployee(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      designation: json['designation'],
      age: json['age'],
      address: json['address'],
      dob: json['dob'],
      salary: json['salary'],
      image: json['image'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'email': email,
      'designation': designation,
      'age': age,
      'address': address,
      'dob': dob,
      'salary': salary,
      'image': image,
    };
  }

}