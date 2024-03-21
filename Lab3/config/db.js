const mongoose =require ('mongoose')
mongoose.set('strictQuery', true)

// const atlat ="mongodb+srv://tranvanvi2855:GMkD9dYYSfV3QOTc@cluster0.hchcdt8.mongodb.net/MyDatabase?retryWrites=true&w=majority"
const atlat ="mongodb+srv://tranvanvi2855:56789ViVi@mydatabase01.zbayvjr.mongodb.net/?retryWrites=true&w=majority&appName=myDatabase01"
const connect = async () =>{
    try{
        await mongoose.connect(atlat,{
            useNewUrlParser: true,
            useUnifiedTopology: true,
        })
        console.log("conncet success");
    }catch (error) {
        console.log("connect fail");
        console.log(error);
    }
}
module.exports = {connect}