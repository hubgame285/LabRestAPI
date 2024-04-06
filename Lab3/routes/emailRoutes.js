var express = require("express");
var router = express.Router();
const Transporter = require("../config/mail");

/* users listing. */
router.post("/test", function (req, res, next) {
  const mailOption = {
    from: "vitvpd06589@fpt.edu.vn",
    to: "vanvi2855@gmail.com",
    subject: "test mail",
    text: "this is a test mail sent NodeJS project",
  };
  Transporter.sendMail(mailOption, function (error, info) {
    if (error) {
      res.status(500).json({ error: "send mail fail" + error });
    } else {
      res.status(200).json({ message: "send mail success" + info.response });
    }
  });
});

module.exports = router;
