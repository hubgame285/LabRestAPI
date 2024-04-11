var express = require("express");
var router = express.Router();
const modelFruits = require("../models/fruits");
const Upload = require("../config/upload");
const JWT = require("jsonwebtoken");
const SECRECT_KEY = "ViTV";

/* GET users listing. */
router.get("/test", function (req, res, next) {
  res.send("respond with a resource fruits test");
});
//add data
router.post("/add", Upload.array("images", 5), async (req, res) => {
  try {
    const { files } = req;
    const urlImage = files.map(
      (file) => `${req.protocol}://${req.get("host")}/uploads/${file.filename}`
    );
    const model = new modelFruits(req.body);
    model.images = urlImage;
    const result = await model.save();
    if (result) {
      res.json({
        status: 200,
        message: "Them thanh cong",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        message: "Them that bai",
        data: [],
      });
    }
  } catch (error) {
    console.log("Error: " + error);
  }
});
//hien thi danh sach
router.get("/list", async (req, res) => {
  try {
    const authHeader = req.headers['authorization']
    console.log('authheader', authHeader)
    const token = authHeader && authHeader.split(' ')[1]
    if(!token) return res.sendStatus(401)
    let payload
    JWT.verify(token, SECRECT_KEY, (err, _payload) => {
      if (err instanceof JWT.TokenExpiredError) return res.sendStatus(401)
      if (err) return res.sendStatus(403)
      payload = _payload
    })
    const result = await modelFruits.find().populate('id_distributor');
    res.send(result);
  } catch (error) {
    console.log(error);
  }
});
//get listByPrice
router.get("/getListByPrice", async (req, res) => {
  try {
    const { start, end } = req.query;
    const query = { price: { $gte: start, $lte: end } };
    const result = await modelFruits
      .find(query, "name price quantity id_distributor")
      .populate("id_distributor")
      .sort({ quantity: -1 })
      .skip(0)
      .limit(2);
    res.send(result);
  } catch (error) {
    console.log(error);
  }
});
//hien thi danh sach theo id
router.get("/getById/:id", async (req, res) => {
  try {
    const result = await modelFruits.findById(req.params.id);
    if (result) {
      res.send(result);
    } else {
      res.json({
        status: 400,
        message: "Khong tim thay id",
        data: [],
      });
    }
  } catch (error) {
    if (error.name === "CastError") {
      res.status(404).send("Invalid ID format");
    } else {
      console.log(error);
      res.status(500).send("Internal Server Error");
    }
  }
});
//update theo id
router.patch("/edit/:id", async (req, res) => {
  try {
    const result = await modelFruits.findByIdAndUpdate(req.params.id, req.body);
    if (result) {
      await result.save();
      res.send(result);
    } else {
      res.json({
        status: 400,
        message: "Khong tim thay id",
        data: [],
      });
    }
  } catch (error) {
    if (error.name === "CastError") {
      res.status(404).send("Invalid ID format");
    } else {
      console.log(error);
      res.status(500).send("Internal Server Error");
    }
  }
});
//xoa theo id
router.delete("/delete/:id", async (req, res) => {
  try {
    const result = await modelFruits.findByIdAndDelete(req.params.id);
    if (result) {
      res.json({
        status: 200,
        message: "Xoa thanh cong",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        message: "Xoa that bai",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});

module.exports = router;
