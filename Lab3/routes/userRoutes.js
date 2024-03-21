var express = require('express');
var router = express.Router();
const modelUser = require('../models/users');

/* GET users listing. */
router.get('/test', function(req, res, next) {
  res.send('respond with a resource user test');
});
//add data
router.post('/add', async(req, res) => {
  try{
      const model = new modelUser(req.body)
      const result =  await model.save()
      if (result){
          res.json({
              "status": 200,
              "message": "Them thanh cong",
              "data": result
          })
      }else{
          res.json({
              "status": 400,
              "message": "Them that bai",
              "data": []
          })
      }
  }catch(error){
      console.log('Error: '+error);
  }
})

module.exports = router;
