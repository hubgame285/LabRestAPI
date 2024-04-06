var express = require("express");
var router = express.Router();
const modelUser = require("../models/users");
const Transporter = require("../config/mail");
const Upload = require("../config/upload");

/* GET users listing. */
router.get("/test", function (req, res, next) {
  res.send("respond with a resource user test");
});
//add data
router.post("/add", Upload.single("avatar"), async (req, res) => {
  try {
    const { file } = req;
    const urlImage = `${req.protocol}://${req.get("host")}/uploads/${
      file.filename
    }`;
    const model = new modelUser(req.body);
    model.avatar = urlImage;
    const result = await model.save();
    if (result) {
      const mailOption = {
        from: "vitvpd06589@fpt.edu.vn",
        to: model.email,
        subject: "Đăng ký thành công",
        text: "Chúc mừng bạn đã đăng ký thành công",
      };
      await Transporter.sendMail(mailOption);
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
  const result = await modelUser.find({});
  try {
    res.send(result);
  } catch (error) {
    console.log(error);
  }
});
//hien thi danh sach theo id
router.get("/getById/:id", async (req, res) => {
  try {
    const result = await modelUser.findById(req.params.id);
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
    const result = await modelUser.findByIdAndUpdate(req.params.id, req.body);
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
    const result = await modelUser.findByIdAndDelete(req.params.id);
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
