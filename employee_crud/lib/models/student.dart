class Student {
  final int? id;
  final String name;
  final String email;
  final String gender;
  final String country;
  final String? imagePath;
  final String? hobbies;

  Student({
    this.id,
    required this.name,
    required this.email,
    required this.gender,
    required this.country,
    this.imagePath,
    this.hobbies
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'email': email,
      'gender': gender,
      'country': country,
      'imagePath': imagePath,
      'hobbies': hobbies,
    };
  }

  factory Student.fromMap(Map<String, dynamic> map) {
    return Student(
      id: map['id'],
      name: map['name'],
      email: map['email'],
      gender: map['gender'],
      country: map['country'],
      imagePath: map['imagePath'],
      hobbies: map['hobbies'],
    );
  }

}