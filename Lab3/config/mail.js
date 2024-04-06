const nodeMailer = require("nodemailer");

const transporter = nodeMailer.createTransport({
  service: "gmail",
  auth: {
    user: "vitvpd06589@fpt.edu.vn", //vitvpd06589@fpt.edu.vn
    pass: "hxry bbit zycj oyxd",
  },
});
module.exports = transporter;
