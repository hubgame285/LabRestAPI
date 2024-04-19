var express = require('express');
var router = express.Router();
const modelDistributor = require('../models/distributors');

/* GET users listing. */
router.get('/test', function(req, res, next) {
  res.send('respond with a resource distributor test');
});
//add data
router.post('/add', async(req, res) => {
  try{
      const model = new modelDistributor(req.body)
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
//hien thi danh sach
router.get('/list', async (req, res) =>{

    try {
        const result = await modelDistributor.find({})
        // res.send(result)
        if (result){
            res.json({
                "status": 200,
                "message": "List",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "message": "Error load list",
                "data": []
            })
        }
    } catch (error) {
        console.log(error)
    }
})
//hien thi danh sach theo id
router.get('/getById/:id', async (req, res) =>{
    try {
        const result = await modelDistributor.findById(req.params.id)
        if(result){
            //res.send(result)
            res.json({
                "status": 200,
                "message": "Da tim thay",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "message": "Khong tim thay id",
                "data": []
            })
        }
    } catch (error) {
        if(error.name === 'CastError'){
            res.status(404).send('Invalid ID format')
        }else{
            console.log(error);
            res.status(500).send('Internal Server Error')
        }
    }
})
//update theo id
router.put('/edit/:id', async(req, res) => {
    try {
        const result = await modelDistributor.findByIdAndUpdate(req.params.id, req.body)
        if(result){
            const rs = await result.save();
            //res.send(result);
            res.json({
                "status": 200,
                "message": "Cap nhat thanh cong",
                "data": rs
            })
        }else{
            res.json({
                "status": 400,
                "message": "Khong tim thay id",
                "data": []
            })
        }
    } catch (error) {
        if(error.name === 'CastError'){
            res.status(404).send('Invalid ID format')
        }else{
            console.log(error);
            res.status(500).send('Internal Server Error')
        }
    }
})
//xoa theo id
router.delete('/delete/:id', async(req, res) => {
    try {
        const result = await modelDistributor.findByIdAndDelete(req.params.id)
        if(result){
            res.json({
                "status": 200,
                "message": "Xoa thanh cong",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "message": "Xoa that bai",
                "data": []
            })
        }
    } catch (error) {
       console.log(error);
    }
})
//search
router.get('/search', async (req, res) =>{

    try {
        const key = req.query.key
        const result = await modelDistributor.find({name:{"$regex": key,"$options":"i"}}).sort({createAt: -1})
        if (result){
            res.json({
                "status": 200,
                "message": "Tim thay",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "message": "Loi khonng co du lieu",
                "data": []
            })
        }
    } catch (error) {
        console.log(error)
    }
})

module.exports = router;
