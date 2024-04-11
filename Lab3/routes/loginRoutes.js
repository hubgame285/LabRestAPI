var express = require("express");
var router = express.Router();
const modelUser = require("../models/users");
const JWT = require("jsonwebtoken");
const SECRECT_KEY = "ViTV";
/* GET users listing. */
router.post("/checkLogin", async (req, res) => {
  try {
    const { username, password } = req.body;
    const user = await modelUser.findOne({ username, password });
    console.log(user);
    if (user) {
      const token = JWT.sign({ id: user._id }, SECRECT_KEY, {expiresIn: "1h"});
      const refreshToken = JWT.sign({ id: user._id }, SECRECT_KEY, {expiresIn: "1d"});
      res.json({
        status: 200,
        message: "dang nhap thanh cong",
        data: user,
        token: token,
        refreshToken: refreshToken,
      });
    } else {
      res.json({
        status: 400,
        message: "dang nhap that bai",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});
module.exports = router;
