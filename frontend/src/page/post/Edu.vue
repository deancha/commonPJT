<template>
  <div class="post">
    <!-- 여기 부분 검색어를 넣은 문구로 어떻게 바꿀지 고민 -->
    <div style="text-align: center; margin: 30px 0 20px 0">
      <h1 style="font-size: 30pt;">Study With Me</h1>
      <MyModal @close="closeModal" v-if="isNode">
        <Curri @close="closeModal" @update="updateClass" :nodeId="nodeId" />
      </MyModal>
      <p>원하는 기술 분야를 선택해주세요.</p>
      <p>커리큘럼을 안내해드립니다.</p>
    </div>
    <div v-if="largeCur.length > 1" style="text-align:center">
      <!-- <div v-if="curCheck != 0 && curCheck == curriculum.length"> -->
      <b-form-select
        v-model="selectLarge"
        :options="largeCur"
        style="width: 20%; max-width: 150px"
        @change="changeSelect"
      ></b-form-select>
      <b-form-select
        v-model="selectSmall"
        :options="smallCur"
        style="width: 20%; max-width: 150px"
        @change="getData"
      ></b-form-select>
    </div>
    <div>
      <div style="width:95%; height: 500px; margin-top: 20px">
        <div style=" height:100%; border: 2px solid #e0e3da;border-radius: 20px;">
          <svg width="100%" height="100%">
            <defs />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as d3 from "d3";
import axios from "axios";
import MyModal from "@/components/common/MyModal.vue";
import Curri from "./curriculum.vue";

export default {
  name: "",
  components: {
    MyModal,
    Curri,
  },
  watch: {
    data: {
      handler(newData) {
        this.updateData();
      },
      deep: true,
    },
    forceProperties: {
      handler(newForce) {
        this.updateForces();
      },
      deep: true,
    },
  },
  created() {
    this.getCir();
    this.getData();
    this.width = window.innerWidth - 10;
    this.height = window.innerHeight - 110;

    this.simulation = d3
      .forceSimulation()
      .force("link", d3.forceLink())
      .force("charge", d3.forceManyBody())
      .force("collide", d3.forceCollide())
      .force("center", d3.forceCenter())
      .force("forceX", d3.forceX())
      .force("forceY", d3.forceY())
      .on("tick", this.tick);
    // Call first time to setup default values
    this.updateForces();
  },
  computed: {
    innerGridSize() {
      return this.gridSize / 10;
    },
    nodes() {
      return this.data.nodes;
    },
    links() {
      return this.data.links;
    },
    // These are needed for captions
    linkTypes() {
      const linkTypes = [];
      this.links.forEach((link) => {
        if (linkTypes.indexOf(link.type) === -1) linkTypes.push(link.type);
      });
      return linkTypes.sort();
    },
    classes() {
      const classes = [];
      this.nodes.forEach((node) => {
        if (classes.indexOf(node.class) === -1) classes.push(node.class);
      });
      return classes.sort();
    },
  },
  methods: {
    changeSelect() {
      this.smallCur = [{ value: "", text: "--중분류--" }];
      this.selectSmall = "";
      if (this.selectLarge) {
        axios
          .get(
            process.env.VUE_APP_API_URL +
              "getcurriculumlist/" +
              this.selectLarge
          )
          .then(({ data }) => {
            if (data.data == "success") {
              data.object.forEach((element) => {
                this.smallCur.push({ value: element, text: element });
              });
              this.getData();
            }
          });
      } else {
        this.getData();
      }
    },
    openModal() {
      this.isNode = true;
    },
    closeModal() {
      this.isNode = false;
    },
    updateClass(id, state) {
      const circle = this.selections.graph.selectAll("circle");
      circle.filter((td) => td.id == id).attr("class", state);
    },
    getCir() {
      axios
        .get(process.env.VUE_APP_API_URL + "getcurriculumlist")
        .then(({ data }) => {
          if (data.data == "success") {
            data.object.forEach((element) => {
              this.largeCur.push({ value: element, text: element });
            });
          }
        });
    },
    getData() {
      this.tempData.nodes = [];
      this.tempData.links = [];
      // 전체 불러오기
      axios
        .post(process.env.VUE_APP_API_URL + "getForUserCurriculum", {
          curriculumlargescale: this.selectLarge ? this.selectLarge : null,
          curriculummediumscale: this.selectSmall ? this.selectSmall : null,
          useremail: this.$store.state.useremail
            ? this.$store.state.useremail
            : "",
        })
        .then(({ data }) => {
          // console.log(data);
          if (data.data == "success") {
            data.object.nodes.forEach((element) => {
              this.tempData.nodes.push({
                name: element.name,
                class: element.classes,
                id: element.nodeid,
              });
            });
            this.tempData.links = data.object.links;
            this.changeData();
          }
        });
    },
    changeData() {
      this.data = this.tempData;
    },
    tick() {
      if (!this.data) {
        return;
      }
      const transform = (d) => {
        return "translate(" + d.x + "," + d.y + ")";
      };

      const link = (d) => {
        // Self-link support
        if (d.source.index === d.target.index) {
          return `M${d.source.x - 1},${d.source.y - 1}A30,30 -10,1,0 ${
            d.target.x + 1
          },${d.target.y + 1}`;
        } else {
          return (
            "M" +
            d.source.x +
            "," +
            d.source.y +
            " L" +
            d.target.x +
            "," +
            d.target.y
          );
        }
      };

      const graph = this.selections.graph;
      graph.selectAll("path").attr("d", link);
      graph.selectAll("circle").attr("transform", transform);
      graph.selectAll("text").attr("transform", transform);

      // this.updateNodeLinkCount();
    },
    updateData() {
      this.simulation.nodes(this.nodes);
      this.simulation.force("link").links(this.links);

      const simulation = this.simulation;
      const graph = this.selections.graph;

      // Links should only exit if not needed anymore
      graph.selectAll("path").data(this.links).exit().remove();

      graph
        .selectAll("path")
        .data(this.links)
        .enter()
        .append("path")
        .attr("class", (d) => "link " + d.type);

      // Nodes should always be redrawn to avoid lines above them
      graph.selectAll("circle").remove();
      graph
        .selectAll("circle")
        .data(this.nodes)
        .enter()
        .append("circle")
        .attr("class", (d) => d.class)
        .call(
          d3
            .drag()
            .on("start", this.nodeDragStarted)
            .on("drag", this.nodeDragged)
            .on("end", this.nodeDragEnded)
        )
        .on("mouseover", this.nodeMouseOver)
        .on("mouseout", this.nodeMouseOut)
        .on("click", this.nodeClick);

      graph.selectAll("text").remove();
      graph
        .selectAll("text")
        .data(this.nodes)
        .enter()
        .append("text")
        .attr("x", 2)
        .attr("y", 10)
        .attr("text-anchor", "start")
        .attr("class", "text")
        .text((d) => d.name);

      // Add 'marker-end' attribute to each path
      const svg = d3.select(this.$el.querySelector("svg"));
      svg
        .selectAll("g")
        .selectAll("path")
        .attr("marker-end", (d) => {
          // Caption items doesn't have source and target
          if (d.source && d.target && d.source.index === d.target.index)
            return "url(#end-self)";
          else return "url(#end)";
        });

      // Update caption every time data changes
      this.updateCaption();
      simulation.alpha(1).restart();
    },
    updateForces() {
      const { simulation, forceProperties, width, height } = this;
      simulation
        .force("center")
        .x(width * forceProperties.center.x)
        .y(height * forceProperties.center.y);
      simulation
        .force("charge")
        .strength(
          forceProperties.charge.strength * forceProperties.charge.enabled
        )
        .distanceMin(forceProperties.charge.distanceMin)
        .distanceMax(forceProperties.charge.distanceMax);
      simulation
        .force("collide")
        .strength(
          forceProperties.collide.strength * forceProperties.collide.enabled
        )
        .radius(forceProperties.collide.radius)
        .iterations(forceProperties.collide.iterations);
      simulation
        .force("forceX")
        .strength(
          forceProperties.forceX.strength * forceProperties.forceX.enabled
        )
        .x(width * forceProperties.forceX.x);
      simulation
        .force("forceY")
        .strength(
          forceProperties.forceY.strength * forceProperties.forceY.enabled
        )
        .y(height * forceProperties.forceY.y);
      simulation
        .force("link")
        .distance(forceProperties.link.distance)
        .iterations(forceProperties.link.iterations);

      // updates ignored until this is run
      // restarts the simulation (important if simulation has already slowed down)
      simulation.alpha(1).restart();
    },
    updateNodeLinkCount() {
      let nodeCount = this.nodes.length;
      let linkCount = this.links.length;

      const highlightedNodes = this.selections.graph.selectAll(
        "circle.highlight"
      );
      const highlightedLinks = this.selections.graph.selectAll(
        "path.highlight"
      );
      if (highlightedNodes.size() > 0 || highlightedLinks.size() > 0) {
        nodeCount = highlightedNodes.size();
        linkCount = highlightedLinks.size();
      }
      this.selections.stats.text(
        "Nodes: " + nodeCount + " / Edges: " + linkCount
      );
    },
    updateCaption() {
      // WARNING: Some gross math will happen here!
      const lineHeight = 30;
      const lineMiddle = lineHeight / 2;
      const captionXPadding = 28;
      const captionYPadding = 5;

      const caption = this.selections.caption;
      caption
        .select("rect")
        .attr(
          "height",
          captionYPadding * 2 +
            lineHeight * (this.classes.length + this.linkTypes.length)
        );

      const linkLine = (d) => {
        const source = {
          x: captionXPadding + 13,
          y:
            captionYPadding +
            (lineMiddle + 1) +
            lineHeight * this.linkTypes.indexOf(d),
        };
        const target = {
          x: captionXPadding - 10,
        };
        return "M" + source.x + "," + source.y + "H" + target.x;
      };

      caption.selectAll("g").remove();
      const linkCaption = caption.append("g");
      linkCaption
        .selectAll("path")
        .data(this.linkTypes)
        .enter()
        .append("path")
        .attr("d", linkLine)
        .attr("class", (d) => "link " + d);

      linkCaption
        .selectAll("text")
        .data(this.linkTypes)
        .enter()
        .append("text")
        .attr("x", captionXPadding + 20)
        .attr(
          "y",
          (d) =>
            captionYPadding +
            (lineMiddle + 5) +
            lineHeight * this.linkTypes.indexOf(d)
        )
        .attr("class", "caption")
        .text((d) => (d = d == "links" ? "다음강좌" : "없는경우"));

      const classCaption = caption.append("g");
      classCaption
        .selectAll("circle")
        .data(this.classes)
        .enter()
        .append("circle")
        .attr("cx", captionXPadding - 2)
        .attr(
          "cy",
          (d) =>
            captionYPadding +
            lineMiddle +
            lineHeight * (this.linkTypes.length + this.classes.indexOf(d))
        )
        .attr("class", (d) => d + " caption-circle");

      classCaption
        .selectAll("text")
        .data(this.classes)
        .enter()
        .append("text")
        .attr("x", captionXPadding + 20)
        .attr(
          "y",
          (d) =>
            captionYPadding +
            (lineMiddle + 5) +
            lineHeight * (this.linkTypes.length + this.classes.indexOf(d))
        )
        .attr("class", "caption")
        .text(
          (d) =>
            (d =
              d == "head"
                ? "목표!"
                : (d =
                    d == "ing"
                      ? "진행중"
                      : (d =
                          d == "none"
                            ? "진행가능"
                            : (d = d == "done" ? "진행완료" : "없는경우"))))
        );

      //here
      const captionWidth = caption.node().getBBox().width;
      const captionHeight = caption.node().getBBox().height;
      const paddingX = 18;
      const paddingY = 12;
      caption.attr("transform", "translate(0,0)");
    },
    zoomed() {
      const transform = d3.event.transform;
      // The trick here is to move the grid in a way that the user doesn't perceive
      // that the axis aren't really moving
      // The actual movement is between 0 and gridSize only for x and y
      const translate =
        (transform.x % (this.gridSize * transform.k)) +
        "," +
        (transform.y % (this.gridSize * transform.k));
      this.selections.grid.attr(
        "transform",
        "translate(" + translate + ") scale(" + transform.k + ")"
      );
      this.selections.graph.attr("transform", transform);

      // Define some world boundaries based on the graph total size
      // so we don't scroll indefinitely
      const graphBox = this.selections.graph.node().getBBox();
      const margin = 200;
      const worldTopLeft = [graphBox.x - margin, graphBox.y - margin];
      const worldBottomRight = [
        graphBox.x + graphBox.width + margin,
        graphBox.y + graphBox.height + margin,
      ];
      this.zoom.translateExtent([worldTopLeft, worldBottomRight]);
    },
    nodeDragStarted(d) {
      if (!d3.event.active) {
        this.simulation.alphaTarget(0.3).restart();
      }
      d.fx = d.x;
      d.fy = d.y;
    },
    nodeDragged(d) {
      d.fx = d3.event.x;
      d.fy = d3.event.y;
    },
    nodeDragEnded(d) {
      if (!d3.event.active) {
        this.simulation.alphaTarget(0.0001);
      }
      d.fx = null;
      d.fy = null;
    },
    nodeMouseOver(d) {
      const graph = this.selections.graph;
      const circle = graph.selectAll("circle");
      const path = graph.selectAll("path");
      const text = graph.selectAll("text");

      const related = [];
      const relatedLinks = [];
      related.push(d);
      this.simulation
        .force("link")
        .links()
        .forEach((link) => {
          if (link.source === d || link.target === d) {
            relatedLinks.push(link);
            if (related.indexOf(link.source) === -1) {
              related.push(link.source);
            }
            if (related.indexOf(link.target) === -1) {
              related.push(link.target);
            }
          }
        });
      circle.classed("faded", true);
      circle
        .filter((df) => related.indexOf(df) > -1)
        .classed("highlight", true);
      path.classed("faded", true);
      path
        .filter((df) => df.source === d || df.target === d)
        .classed("highlight", true);
      text.classed("faded", true);
      text.filter((df) => related.indexOf(df) > -1).classed("highlight", true);
      // This ensures that tick is called so the node count is updated
      this.simulation.alphaTarget(0.0001).restart();
    },
    nodeMouseOut(d) {
      const graph = this.selections.graph;
      const circle = graph.selectAll("circle");
      const path = graph.selectAll("path");
      const text = graph.selectAll("text");

      circle.classed("faded", false);
      circle.classed("highlight", false);
      path.classed("faded", false);
      path.classed("highlight", false);
      text.classed("faded", false);
      text.classed("highlight", false);
      // This ensures that tick is called so the node count is updated
      this.simulation.restart();
    },
    nodeClick(d) {
      this.nodeId = d.id;
      this.openModal();
      const circle = this.selections.graph.selectAll("circle");
      circle.classed("selected", false);
      circle.filter((td) => td === d).classed("selected", true);
    },
  },
  mounted() {
    this.selections.svg = d3.select(this.$el.querySelector("svg"));
    const svg = this.selections.svg;

    // Define the arrow marker
    svg
      .append("svg:defs")
      .selectAll("marker")
      .data(["end"]) // Different link/path types can be defined here
      .enter()
      .append("svg:marker") // This section adds in the arrows
      .attr("id", String)
      .attr("viewBox", "0 -5 10 10")
      .attr("refX", 20) // Prevents arrowhead from being covered by circle
      .attr("refY", 0)
      .attr("markerWidth", 6)
      .attr("markerHeight", 6)
      .attr("orient", "auto")
      .append("svg:path")
      .attr("d", "M0,-5L10,0L0,5");

    // Define arrow for self-links
    svg
      .append("svg:defs")
      .selectAll("marker")
      .data(["end-self"])
      .enter()
      .append("svg:marker") // This section adds in the arrows
      .attr("id", String)
      .attr("viewBox", "0 -5 10 10")
      .attr("refX", 40)
      .attr("refY", -15)
      .attr("markerWidth", 6)
      .attr("markerHeight", 6)
      .attr("orient", 285)
      .append("svg:path")
      .attr("d", "M0,-5L10,0L0,5");

    // Add zoom and panning triggers
    this.zoom = d3
      .zoom()
      .scaleExtent([1 / 4, 4])
      .on("zoom", this.zoomed);
    svg.call(this.zoom);

    // A background grid to help user experience
    // The width and height depends on the minimum scale extent and
    // the + 10% and negative index to create an infinite grid feel
    // The precedence of this element is important since you'll have
    // click events on the elements above the grid
    this.selections.grid = svg
      .append("rect")
      .attr("x", "-10%")
      .attr("y", "-10%")
      .attr("width", "410%")
      .attr("height", "410%")
      .attr("fill", "url(#grid)");

    this.selections.graph = svg.append("g");
    const graph = this.selections.graph;

    // Node and link count is nice :)
    this.selections.stats = svg
      .append("text")
      .attr("x", "1%")
      .attr("y", "98%")
      .attr("text-anchor", "left");

    // Some caption
    this.selections.caption = svg.append("g");
    this.selections.caption
      .append("rect")
      .attr("width", "200")
      .attr("height", "0")
      .attr("rx", "10")
      .attr("ry", "10")
      .attr("class", "caption");
  },
  data: () => {
    return {
      isNode: false,
      nodeId: null,
      selectLarge: "",
      selectSmall: "",
      largeCur: [{ value: "", text: "--대분류--" }],
      smallCur: [{ value: "", text: "--중분류--" }],
      data: null,
      tempData: {
        nodes: [],
        links: [],
      },
      d: [{ ing: "진행중", done: "학습완료", none: "학습가능" }],
      gridSize: 100,
      selections: {},
      simulation: null,
      forceProperties: {
        center: {
          x: 0.3,
          y: 0.4,
        },
        charge: {
          enabled: true,
          strength: -700,
          distanceMin: 1,
          distanceMax: 2000,
        },
        collide: {
          enabled: true,
          strength: 0.7,
          iterations: 1,
          radius: 35,
        },
        forceX: {
          enabled: true,
          strength: 0.05,
          x: 0.5,
        },
        forceY: {
          enabled: true,
          strength: 0.35,
          y: 0.5,
        },
        link: {
          enabled: true,
          distance: 100,
          iterations: 1,
        },
      },
    };
  },
};
</script>
<style>
.tag button {
  height: fit-content;
  width: fit-content;
  box-shadow: none;
  border: 1px solid #a593e0;
  font-size: 12pt;
  margin-right: 10px;
  padding: 5px 10px 5px 10px;
  border-radius: 2em;
  background-color: #eae5f8;
  color: #566270 !important;
}
.tag button:hover {
  background-color: #b9c3ff;
}
.btn-secondary {
  background-color: white !important;
  border: 0 !important;
}
.faded {
  opacity: 0.1;
  transition: 0.3s opacity;
}
.highlight {
  opacity: 1;
}

path.link {
  fill: none;
  stroke: #666;
  stroke-width: 1.5px;
}
path.link.depends {
  /* stroke: #005900; */
  /* stroke-dasharray: 5, 2; */
}
path.link.needs {
  /* stroke: #7f3f00; */
}

circle {
  fill: #ffff99;
  stroke: #191900;
  stroke-width: 1.5px;
}

circle.head {
  r: 12;
  fill: #353866;
  stroke: #4f3e9b;
}
circle.tail {
  r: 12;
  fill: #dfb2d7;
  stroke: #3b7fad;
}
circle.none {
  r: 8;
  fill: #e8dcf8;
  stroke: #b9b1e1;
}
circle.ing {
  r: 9;
  fill: #d1baf1;
  stroke: #6952b4;
}
circle.done {
  r: 10;
  fill: #a172e3;
  stroke: #6f746f;
}

circle.selected {
  stroke: #c961c1 !important;
  stroke-width: 2px;
  animation: selected 2s infinite alternate ease-in-out;
}
circle.caption-circle {
  r: 8;
}
@keyframes selected {
  from {
    stroke-width: 5px;
    r: 16;
  }
  to {
    stroke-width: 1px;
    r: 15;
  }
}

.text {
  font: 13px sans-serif;
  pointer-events: none;

  font-family: "NEXONGothic";
  text-shadow: 0 1px 0 #fff, 1px 0 0 #fff, 0 -1px 0 #fff, -1px 0 0 #fff;
}

rect.caption {
  fill: #eeeeeeac;
  stroke: #b7b4b4;
  stroke-width: 1px;
}
.text.caption {
  font-size: 12px;
  font-weight: bol;
}
</style>
