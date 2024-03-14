var _cal = require('./caculator')
 
var express = require('express');
var app = express();

app.get('/calADD', function(req, res){
    var rs = 0;
    var a = parseInt(req.query.a);
    var b = parseInt(req.query.b);
    rs = _cal.add(a,b);
    res.json(rs);
});

app.get('/calSUB', function(req, res){
    var rs = 0;
    var a = parseInt(req.query.a);
    var b = parseInt(req.query.b);
    rs = _cal.sub(a,b);
    res.json(rs);
});

app.get('/calMUL', function(req, res){
    var rs = 0;
    var a = parseInt(req.query.a);
    var b = parseInt(req.query.b);
    rs = _cal.mul(a,b);
    res.json(rs);
});

app.get('/calDIV', function(req, res){
    var rs = 0;
    var a = parseInt(req.query.a);
    var b = parseInt(req.query.b);
    rs = _cal.div(a,b);
    res.json(rs);
});
app.listen(8080);

// var a = 9;
// var b = 3;
// console.log('ket qua cong:'+ _cal.add(a,b));
// console.log('ket qua tru:'+ _cal.sub(a,b));
// console.log('ket qua nhan:'+ _cal.mul(a,b));
// console.log('ket qua chia:'+ _cal.div(a,b));