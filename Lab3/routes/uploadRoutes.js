var express = require("express");
var router = express.Router();
const Upload = require("../config/upload");

/* users listing. */
router.post("/test", function (req, res, next) {
  res.send("respond with a resource upload file");
});
router.post("/mulUpload", Upload.array("images", 5), async (req, res) => {
  try {
    const { files } = req;
    const urlImage = files.map(
      (file) => `${req.protocol}://${req.get("host")}/uploads/${file.filename}`
    );
    console.log(urlImage);
  } catch (error) {
    console.log(error);
  }
});

module.exports = router;
