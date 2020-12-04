<template>
 <div class="container">
    <div class="load-wrapp">
      <div class="load-3">
        <p class="text">유용한 코딩 관련 
            <br>블로그 글을 수집 중입니다 <br>
            잠시만 기다려주세요 </p>
        <div class="line"></div>
        <div class="line"></div>
        <div class="line"></div>
      </div>
    </div>
    </div>
</template>

<script>

export default {
  name: "PreLoader"
}
</script>

<style scoped>
/* -----------------------------------------
  =Default css to make the demo more pretty
-------------------------------------------- */

   
.container {

  margin: 0;
  padding:0;
  overflow-y: scroll;
  font-weight: 400;
  color: #777;
  background-color: #f7f7f7;
  -webkit-font-smoothing: antialiased;
  -webkit-text-size-adjust: 100%;
  -ms-text-size-adjust: 100%;
  justify-content: center;
display: flex;

  
}

.content {
  padding: 15px;
  overflow: hidden;
  background-color: #e7e7e7;
}

h1 {
  padding-bottom: 15px;
  border-bottom: 1px solid #d8d8d8;
  font-weight: 600;
}

h1 span {
  font-family: monospace, serif;
}

h3 {
  padding-bottom: 20px;
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.1), 0 2px 0 rgba(255, 255, 255, 0.9);
}

p {
  margin: 0;
  color: #777;
   font-size: 1rem;

}

.clear {
  clear: both;
}

/* -----------------------------------------
  =CSS3 Loading animations
-------------------------------------------- */

/* =Elements style
---------------------- */
.load-wrapp {
  height: 100vh;
  margin: 0;
  padding: 0;
  border-radius: 5px;
  text-align: center;
  position: absolute;
  top: 50%;
  /* background-color: #d8d8d8; */

}

.load-wrapp p {
  padding: 0 0 20px;
}
.load-wrapp:last-child {
  margin-right: 0;
}

.line {
  display: inline-block;
  width: 15px;
  height: 15px;
  border-radius: 50%;
  background-color:  #a593e0;
  padding:2px;
}

.letter {
  float: left;
  font-size: 14px;
  color: #777;
}


.spinner {
  position: relative;
  width: 45px;
  height: 45px;
  margin: 0 auto;
}

.load-3 .line:nth-last-child(1) {
  animation: loadingC 0.6s 0.1s linear infinite;
}
.load-3 .line:nth-last-child(2) {
  animation: loadingC 0.6s 0.2s linear infinite;
}
.load-3 .line:nth-last-child(3) {
  animation: loadingC 0.6s 0.3s linear infinite;
}


@keyframes loadingC {
  0 {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(0, 15px);
  }
  100% {
    transform: translate(0, 0);
  }
}






</style>