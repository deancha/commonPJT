<template>
  <section>
    <div class="teamintroduction">
      <header class="major">
        <h2 class="dohyeon">멋쟁이 상원처럼?</h2>
      </header>
      <div class="features">
        <article>
          <span class="icon fa-map"></span>
          <div class="content dohyeon">
            <h3 class="dohyeon">신기술 및 알고리즘 문제 풀이 포스팅 제공</h3>
            <p class="dohyeon">현업의 각종 신기술 및 어려운 문제에 대한 풀이를 제공합니다. 맘껏 공유하셔도 좋습니다</p>
          </div>
        </article>
        <article>
          <span class="icon fa-map"></span>
          <div class="content">
            <h3 class="dohyeon">협업 시 필요한 기술 로드맵 제공</h3>
            <p
              class="dohyeon"
            >내가 WEB에 대해 공부하고 싶다? 어디서부터 공부해야 할 지 막막할 때가 있으실 수 있습니다. 선행기술들의 관계를 직관적으로 보고 선택하실 수 있습니다.</p>
          </div>
        </article>
        <article>
          <span class="icon fa fa-map"></span>
          <div class="content">
            <h3 class="dohyeon">타회원 학습진도 트래킹 기능</h3>
            <p
              class="dohyeon"
            >스터디에 참여하긴 부담스럽고 혼자 하기엔 자극이 필요하신 분들을 위해 준비했습니다. 아무런 부담없이 트래킹 기능에 참여할 수 있고 다른 회원들의 학습진도를 확인할 수 있습니다.</p>
          </div>
        </article>
        <article></article>
        <!-- <article>
			<span class="icon fa-signal"></span>
			<div class="content">
				<h3>Sed magna finibus</h3>
				<p>Aenean ornare velit lacus, ac varius enim lorem ullamcorper dolore. Proin aliquam facilisis ante interdum. Sed nulla amet lorem feugiat tempus aliquam.</p>
			</div>
        </article>-->
      </div>
    </div>
    <br />
    <br />
    <br />

    <div class="parkwithoutpark">
      <header class="major">
        <h2 class="dohyeon">Park without Park</h2>
      </header>

      <div>
        <b-card-group columns>
          <b-card :img-src="Sumin" class="dohyeon" img-alt="Image" img-top>
            <b-card-title class="dohyeon">김수민</b-card-title>
            <b-card-text class="dohyeon">
              디자인 어려워요ㅠ
              <br />frontend developer
            </b-card-text>
            <b-button href="https://github.com/KSM0805" target="_blank">GitHub</b-button>
          </b-card>

          <b-card class="dohyeon" :img-src="Seonyeong" img-alt="Image" img-top>
            <b-card-title class="dohyeon">박선영</b-card-title>
            <b-card-text class="dohyeon">
              꿈꾸는 개발자 로시입니다.
              <br />frontend developer
            </b-card-text>
            <b-button href="https://github.com/lociworld" target="_blank">GitHub</b-button>
          </b-card>

          <b-card :img-src="Sangwon" img-alt="Image" img-top class="dohyeon">
            <b-card-title class="dohyeon">서상원</b-card-title>
            <b-card-text class="dohyeon">
              네, 제가 그 서상원입니다.
              <br />backend developer
            </b-card-text>
            <b-button href="https://github.com/sangwonseo94" target="_blank">GitHub</b-button>
          </b-card>

          <b-card>
            <blockquote class="card-blockquote">
              <p>커뮤니케이션이 활발한 멋진 팀입니다. 공통 프로젝트 진행하며 많이 배우고 있습니다!</p>
              <footer>
                <small>석우형도 화이팅!</small>
              </footer>
            </blockquote>
          </b-card>

          <b-card class="dohyeon" :img-src="Hyemin" img-alt="Image" img-top>
            <b-card-title class="dohyeon">신혜민</b-card-title>
            <b-card-text class="dohyeon">
              개발빼고 다 쉬워요
              <br />frontend developer
            </b-card-text>
            <b-button href="https://github.com/min0907" target="_blank">GitHub</b-button>
          </b-card>

          <b-card class="dohyeon" :img-src="Dongwoo" img-alt="Image" img-top>
            <b-card-title class="dohyeon">차동우</b-card-title>
            <b-card-text class="dohyeon">
              서상원!서상원!서상원!서상원!
              <br />backend developer
            </b-card-text>
            <b-button href="https://github.com/deancha" target="_blank">GitHub</b-button>
          </b-card>
        </b-card-group>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data: () => {
    return {
      Sangwon: require("@/assets/img/sangwon2.jpg"),
      Dongwoo: require("@/assets/img/dongwoo2.jpg"),
      Seonyeong: require("@/assets/img/seonyeong.jpg"),
      Sumin: require("@/assets/img/sumin.jpg"),
      Hyemin: require("@/assets/img/hyemin.png"),
    };
  },
};
</script>

<style>
</style>