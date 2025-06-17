import 'package:decimal/decimal.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const CalculatorApp());
}

class CalculatorApp extends StatelessWidget {
  const CalculatorApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Basic Calculator',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: CalculatorScreen(),
    );
  }
}

class CalculatorScreen extends StatefulWidget {
  const CalculatorScreen({super.key});

  @override
  State<CalculatorScreen> createState() => _CalculatorScreenState();
}

class _CalculatorScreenState extends State<CalculatorScreen> {
  String _output = "0";
  String _currentInput = "";
  double _num1 = 0;
  double _num2 = 0;
  String _operation = "";

  void _buttonPressed(String buttonText) {
    setState(() {
      if (buttonText == "C") {
        _output = "0";
        _currentInput = "";
        _num1 = 0;
        _num2 = 0;
        _operation = "";

      } else if (buttonText == "+" || buttonText == "-" || buttonText == "x" || buttonText == "÷") {
        _num1 = double.parse(_currentInput);

        _operation = buttonText;
        _currentInput = "";
        _output = "$_num1 $_operation";

      } else if (buttonText == "=") {
        _num2 = double.parse(_currentInput);

        switch(_operation) {
          case "+":
            _currentInput = (Decimal.parse(_num1.toString()) + Decimal.parse(_num2.toString())).toString();
            break;
          case "-":
            _currentInput = (Decimal.parse(_num1.toString()) - Decimal.parse(_num2.toString())).toString();
            break;
          case "x":
            _currentInput = (Decimal.parse(_num1.toString()) * Decimal.parse(_num2.toString())).toString();
            break;
          case "÷":
            _currentInput = (Decimal.parse(_num1.toString()) / Decimal.parse(_num2.toString())).toString();
        }

        _num1 = 0;
        _num2 = 0;
        _operation = "";
        _output = _currentInput;

      } else if (buttonText == "⌫") {
        if (_currentInput.isNotEmpty) {
          _currentInput = _currentInput.substring(0, _currentInput.length - 1);
          if (_currentInput.isEmpty) {
            _currentInput = "";
          }
        } else {
          _currentInput = "";
        }

        if (_currentInput == "") {
          _output = "0";
        } else {
          _output = _currentInput;
        }

      } else {
        _currentInput += buttonText;
        _output = _currentInput;
      }
    });
  }

  Widget _buildButton(
    String buttonText, {
      Color color = Colors.white,
      Color textColor = Colors.black,
  }) {
    return Expanded(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: ElevatedButton(
          onPressed: () => _buttonPressed(buttonText),
          style: ElevatedButton.styleFrom(
            backgroundColor: color,
            padding: EdgeInsets.all(24.0),
          ),
          child: Text(
            buttonText,
            style: TextStyle(fontSize: 24.0, color: textColor),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Basic Calculator'), backgroundColor: Colors.orange,),
      body: Column(
        children: <Widget>[
          Container(
            alignment: Alignment.centerRight,
            padding: EdgeInsets.symmetric(vertical: 24.0, horizontal: 12.0),
            child: Text(
              _output,
              style: TextStyle(fontSize: 48.0, fontWeight: FontWeight.bold),
            ),
          ),
          Expanded(child: Divider()),
          Column(
            children: [
              Row(
                children: [
                  _buildButton(
                    "C",
                    color: Colors.grey,
                  ),
                  _buildButton(""),
                  _buildButton(""),
                  _buildButton(
                    "⌫",
                    color: Colors.redAccent,
                    textColor: Colors.white,
                  )
                ],
              ),
              Row(
                children: [
                  _buildButton("7"),
                  _buildButton("8"),
                  _buildButton("9"),
                  _buildButton(
                    "÷",
                    color: Colors.orange,
                    textColor: Colors.white,
                  ),
                ],
              ),
              Row(
                children: [
                  _buildButton("4"),
                  _buildButton("5"),
                  _buildButton("6"),
                  _buildButton(
                    "x",
                    color: Colors.orange,
                    textColor: Colors.white,
                  ),
                ],
              ),
              Row(
                children: [
                  _buildButton("1"),
                  _buildButton("2"),
                  _buildButton("3"),
                  _buildButton(
                    "-",
                    color: Colors.orange,
                    textColor: Colors.white,
                  ),
                ],
              ),
              Row(
                children: [
                  _buildButton("."),
                  _buildButton("0"),
                  _buildButton(
                    "=",
                    color: Colors.orange,
                    textColor: Colors.white,
                  ),
                  _buildButton(
                    "+",
                    color: Colors.orange,
                    textColor: Colors.white,
                  ),
                ],
              ),
            ],
          ),
        ],
      ),
    );
  }

}
