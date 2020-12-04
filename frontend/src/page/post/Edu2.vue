<template>
  <div class="post">
    <!-- 여기 부분 검색어를 넣은 문구로 어떻게 바꿀지 고민 -->
    <button v-if="isDetail" @click="changeData()">돌아가기</button>
    <div style="width:95%; height: 500px">
      <!-- 옵시디언 -->
      <!-- 강의 링크나 별점을 여기서 모달로?  -->
      <!-- **본인만 참가한거** -->
      <div style=" height:100%">
        <svg width="100%" height="100%">
          <defs>
            <pattern
              id="innerGrid"
              :width="innerGridSize"
              :height="innerGridSize"
              patternUnits="userSpaceOnUse"
            >
              <rect
                width="100%"
                height="100%"
                fill="none"
                stroke="#CCCCCC7A"
                stroke-width="0.5"
              />
            </pattern>
            <pattern
              id="grid"
              :width="gridSize"
              :height="gridSize"
              patternUnits="userSpaceOnUse"
            >
              <rect
                width="100%"
                height="100%"
                fill="url(#innerGrid)"
                stroke="#CCCCCC7A"
                stroke-width="1.5"
              />
            </pattern>
          </defs>
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
import * as d3 from "d3";
export default {
  name: "",
  components: {},
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
    // d3 데이터 바꾸기
    this.changeData();
    // You can set the component width and height in any way
    // you prefer. It's responsive! :)
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
    changeData(data) {
      // console.log(data);
      switch (data) {
        case null:
          this.isDetail = false;
          this.data = this.jsondata;
          break;
        case "spring":
          this.isDetail = true;
          this.data = this.javaData;
          break;
        case "vue":
          this.isDetail = true;
          this.data = this.detailData;
          break;
        case "algo":
          this.isDetail = true;
          this.data = this.algoData;
          break;
        default:
          this.isDetail = false;
          this.data = this.jsondata;
      }
    },
    tick() {
      // If no data is passed to the Vue component, do nothing
      if (!this.data) {
        return;
      }
      const transform = (d) => {
        return "translate(" + d.x + "," + d.y + ")";
      };

      const link = (d) => {
        // Self-link support
        if (d.source.index === d.target.index) {
          return `M${d.source.x - 1},${d.source.y - 1}A30,30 -10,1,0 ${d.target
            .x + 1},${d.target.y + 1}`;
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

      this.updateNodeLinkCount();
    },
    updateData() {
      this.simulation.nodes(this.nodes);
      this.simulation.force("link").links(this.links);

      const simulation = this.simulation;
      const graph = this.selections.graph;

      // Links should only exit if not needed anymore
      graph
        .selectAll("path")
        .data(this.links)
        .exit()
        .remove();

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
        .attr("x", 0)
        .attr("y", ".31em")
        .attr("text-anchor", "middle")
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
      // this.updateCaption();
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
        .text((d) => d);

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
        .text((d) => d);

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
      console.log(d);
      const circle = this.selections.graph.selectAll("circle");
      circle.classed("selected", false);
      circle.filter((td) => td === d).classed("selected", true);
      if (d.index == 47) {
        // 스프링
        this.changeData("spring");
      } else if (d.index == 63) {
        // 알고리즘
        this.changeData("algo");
      }
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
      .attr("refX", 43) // Prevents arrowhead from being covered by circle
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
      isDetail: false,
      data: null,
      jsondata: {
        nodes: [
          // class : Backend, Frontend, Algorithm, Stack -> classes

          { name: "인터넷", group: 1, class: "Frontend" },
          { name: "HTML", group: 1, class: "Frontend" },
          { name: "CSS", group: 1, class: "Frontend" },
          { name: "JavaScript", group: 1, class: "Frontend" },
          { name: "API/Ajax", group: 2, class: "Stack" },
          {
            name: "ES6+",
            group: 2,
            class: "Stack",
          },
          { name: "버전 관리 시스템", group: 1, class: "Frontend" },
          { name: "Git 사용법", group: 2, class: "Stack" },

          { name: "패키지 관리자", group: 1, class: "Frontend" },
          { name: "npm", group: 2, class: "Stack" },
          { name: "yarn", group: 2, class: "Stack" },
          // 10
          { name: "웹 보안 지식", group: 1, class: "Frontend" },

          { name: "빌드 툴", group: 1, class: "Frontend" },
          { name: "ESLint", group: 1, class: "Stack" },
          { name: "Prettier", group: 1, class: "Stack" },
          { name: "Webpack", group: 1, class: "Stack" },

          { name: "프레임워크", group: 1, class: "Frontend" },
          { name: "React.js", group: 2, class: "Stack" },
          { name: "Angular", group: 1, class: "Stack" },
          { name: "Vue.js", group: 1, class: "Stack" },
          { name: "CSS 프레임워크", group: 1, class: "Frontend" },
          // 20
          { name: "Backend", class: "Backend" },
          { name: "Frontend", class: "Frontend" },
          { name: "HTTP", class: "Stack" },
          { name: "DNS", class: "Stack" },
          { name: "Network", class: "Stack" },
          { name: "운영체제", class: "Backend" },
          { name: "프로그래밍언어", class: "Backend" },
          { name: "Java", class: "Stack" },
          { name: "C#", class: "Stack" },
          // 29
          { name: "PHP", class: "Stack" },
          { name: "Rust", group: 1, class: "Stack" },
          { name: "Go", group: 1, class: "Stack" },
          { name: "JavaScript", group: 1, class: "Stack" },
          { name: "Python", group: 1, class: "Stack" },
          { name: "Ruby", group: 1, class: "Stack" },
          // 35
          { name: "DataBase", group: 2, class: "Backend" },
          { name: "Relative DB", group: 2, class: "Backend" },
          { name: "MariaDB", group: 2, class: "Stack" },
          { name: "MySQL", group: 2, class: "Stack" },
          // 39
          { name: "Oracle", group: 2, class: "Stack" },
          { name: "NoSQL DB", group: 2, class: "Backend" },
          { name: "MongoDB", group: 2, class: "Stack" },
          { name: "RethinkDB", group: 2, class: "Stack" },
          { name: "CouchDB", group: 2, class: "Stack" },
          { name: "DynamoDB", group: 2, class: "Stack" },
          // 45
          { name: "FrameWork", group: 2, class: "Backend" },
          { name: "Java Spring", group: 2, class: "Stack" },
          { name: "Django", group: 2, class: "Stack" },
          { name: "Node.js", group: 2, class: "Stack" },
          { name: "API", group: 2, class: "Backend" },
          // 50
          { name: "REST", group: 2, class: "Stack" },
          { name: "JSON APIs", group: 2, class: "Stack" },
          //52
          { name: "HTTPS", group: 2, class: "Stack" },
          { name: "CORS", group: 2, class: "Stack" },
          { name: "CI/CD", group: 2, class: "Backend" },
          { name: "Jenkins", group: 2, class: "Stack" },
          { name: "가상화 컨테이너 기술", group: 2, class: "Backend" },
          // 57
          { name: "Docker", group: 2, class: "Stack" },
          { name: "웹 서버", group: 2, class: "Backend" },
          // 59
          { name: "Nginx", group: 2, class: "Stack" },
          { name: "Apache", group: 2, class: "Stack" },
          { name: "Caddy", group: 2, class: "Stack" },
          { name: "Algorithm", group: 2, class: "Algorithm" },
        ],
        links: [
          { source: 1, target: 0, value: 1, type: "link" },
          { source: 2, target: 1, value: 1, type: "link" },
          { source: 3, target: 2, value: 1, type: "link" },
          { source: 4, target: 3, value: 1, type: "link" },
          { source: 5, target: 3, value: 1, type: "link" },

          { source: 6, target: 3, value: 1, type: "link" },
          { source: 7, target: 6, value: 1, type: "link" },
          { source: 8, target: 6, value: 1, type: "link" },
          { source: 9, target: 8, value: 1, type: "link" },
          { source: 10, target: 8, value: 1, type: "link" },
          { source: 11, target: 8, value: 1, type: "link" },
          { source: 12, target: 11, value: 1, type: "link" },
          { source: 13, target: 12, value: 1, type: "link" },
          { source: 14, target: 12, value: 1, type: "link" },
          { source: 15, target: 12, value: 1, type: "link" },
          { source: 16, target: 12, value: 1, type: "link" },
          { source: 17, target: 16, value: 1, type: "link" },
          { source: 18, target: 16, value: 1, type: "link" },
          { source: 19, target: 16, value: 1, type: "link" },
          { source: 20, target: 16, value: 1, type: "link" },
          // 여기까지 혜미니
          { source: 22, target: 21, value: 1, type: "link" },
          { source: 22, target: 0, value: 1, type: "link" },
          { source: 21, target: 0, value: 1, type: "link" },
          { source: 23, target: 0, value: 1, type: "link" },
          { source: 24, target: 0, value: 1, type: "link" },
          { source: 25, target: 0, value: 1, type: "link" },
          { source: 26, target: 0, value: 1, type: "link" },
          { source: 27, target: 26, value: 1, type: "link" },
          { source: 28, target: 27, value: 1, type: "link" },
          { source: 29, target: 27, value: 1, type: "link" },
          { source: 30, target: 27, value: 1, type: "link" },
          { source: 31, target: 27, value: 1, type: "link" },
          { source: 32, target: 27, value: 1, type: "link" },
          { source: 33, target: 27, value: 1, type: "link" },
          { source: 34, target: 27, value: 1, type: "link" },
          { source: 35, target: 27, value: 1, type: "link" },

          { source: 36, target: 27, value: 1, type: "link" },
          { source: 37, target: 36, value: 1, type: "link" },
          { source: 38, target: 37, value: 1, type: "link" },

          { source: 39, target: 37, value: 1, type: "link" },
          { source: 40, target: 37, value: 1, type: "link" },
          { source: 41, target: 36, value: 1, type: "link" },
          { source: 42, target: 41, value: 1, type: "link" },
          { source: 43, target: 41, value: 1, type: "link" },
          { source: 44, target: 41, value: 1, type: "link" },
          { source: 45, target: 41, value: 1, type: "link" },
          { source: 46, target: 36, value: 1, type: "link" },
          { source: 47, target: 46, value: 1, type: "link" },
          { source: 48, target: 46, value: 1, type: "link" },
          { source: 49, target: 46, value: 1, type: "link" },
          { source: 50, target: 46, value: 1, type: "link" },
          { source: 51, target: 50, value: 1, type: "link" },
          { source: 52, target: 50, value: 1, type: "link" },

          { source: 11, target: 50, value: 1, type: "link" },
          { source: 53, target: 11, value: 1, type: "link" },
          { source: 54, target: 11, value: 1, type: "link" },
          { source: 55, target: 11, value: 1, type: "link" },
          { source: 56, target: 55, value: 1, type: "link" },
          { source: 57, target: 55, value: 1, type: "link" },
          { source: 58, target: 57, value: 1, type: "link" },
          { source: 59, target: 57, value: 1, type: "link" },
          { source: 60, target: 59, value: 1, type: "link" },
          { source: 61, target: 59, value: 1, type: "link" },
          { source: 62, target: 59, value: 1, type: "link" },
        ],
      },
      algoData: {
        nodes: [
          {
            name: "코딩테스트를 위한 자바 알고리즘",
            class: "Frontend",
          },
          {
            name: "Prim 알고리즘",
            class: "Algorithm",
          },
          {
            name: "Kruscal 알고리즘",
            class: "Algorithm",
          },
          {
            name: "Back Tracking",
            class: "Algorithm",
          },
          {
            name: "문자열 알고리즘",
            class: "Algorithm",
          },
          {
            name: "Union Find",
            class: "Algorithm",
          },
          {
            name: "DFS",
            class: "Algorithm",
          },
          {
            name: "BFS",
            class: "Algorithm",
          },
          {
            name: "Stack",
            class: "Algorithm",
          },
          {
            name: "Queue",
            class: "Algorithm",
          },
          {
            name: "LinkedList",
            class: "Algorithm",
          },
          {
            name: "Map",
            class: "Algorithm",
          },
          {
            name: "Dynamic Programming",
            class: "Algorithm",
          },
          {
            name: "Java Array",
            class: "Algorithm",
          },
          {
            name: "Greedy",
            class: "Algorithm",
          },
          {
            name: "자바 자료형",
            class: "Algorithm",
          },
          {
            name: "자바 기본 입출력",
            class: "Backend",
          },
        ],
        links: [
          {
            type: "link",
            value: 1,
            target: 0,
            source: 1,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 2,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 3,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 7,
          },

          {
            type: "link",
            value: 1,
            target: 0,
            source: 10,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 11,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 4,
          },
          {
            type: "link",
            value: 1,
            target: 1,
            source: 5,
          },
          {
            type: "link",
            value: 1,
            target: 2,
            source: 5,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 1,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 4,
          },
          {
            type: "link",
            value: 1,
            target: 3,
            source: 6,
          },
          {
            type: "link",
            value: 1,
            target: 6,
            source: 8,
          },
          {
            type: "link",
            value: 1,
            target: 7,
            source: 9,
          },
          {
            type: "link",
            value: 1,
            target: 4,
            source: 12,
          },
          {
            type: "link",
            value: 1,
            target: 5,
            source: 13,
          },
          {
            type: "link",
            value: 1,
            target: 8,
            source: 13,
          },
          {
            type: "link",
            value: 1,
            target: 9,
            source: 13,
          },
          {
            type: "link",
            value: 1,
            target: 10,
            source: 13,
          },
          {
            type: "link",
            value: 1,
            target: 11,
            source: 15,
          },
          {
            type: "link",
            value: 1,
            target: 12,
            source: 14,
          },
          {
            type: "link",
            value: 1,
            target: 13,
            source: 14,
          },
          {
            type: "link",
            value: 1,
            target: 13,
            source: 15,
          },
          {
            type: "link",
            value: 1,
            target: 15,
            source: 16,
          },
        ],
      },
      javaData: {
        nodes: [
          {
            name: "Spring Project",
            class: "Frontend",
            id: 1,
          },
          {
            name: "Spring Boot",
            class: "Algorithm",
            id: 2,
          },
          {
            name: "Mybatis",
            class: "Algorithm",
          },
          {
            name: "JPA",
            class: "Algorithm",
          },
          {
            name: "Rest API",
            class: "Algorithm",
          },
          {
            name: "Spring Security",
            class: "Algorithm",
          },
          {
            name: "JSON",
            class: "Algorithm",
          },
          {
            name: "DB 연결",
            class: "Algorithm",
          },
          {
            name: "MVC 패턴",
            class: "Algorithm",
          },
          {
            name: "Dispatcher Servlet",
            class: "Algorithm",
          },
          {
            name: "Spring PSA",
            class: "Algorithm",
          },
          {
            name: "AOP",
            class: "Algorithm",
          },
          {
            name: "DI/IOC",
            class: "Algorithm",
          },
          {
            name: "Bean,POJO",
            class: "Algorithm",
          },
        ],
        links: [
          {
            type: "link",
            value: 1,
            target: 0,
            source: 1,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 2,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 3,
          },
          {
            type: "link",
            value: 1,
            target: 0,
            source: 4,
          },

          {
            type: "link",
            value: 1,
            target: 0,
            source: 5,
          },
          {
            type: "link",
            value: 1,
            target: 4,
            source: 6,
          },
          {
            type: "link",
            value: 1,
            target: 3,
            source: 7,
          },
          {
            type: "link",
            value: 1,
            target: 2,
            source: 7,
          },
          {
            type: "link",
            value: 1,
            target: 7,
            source: 8,
          },
          {
            type: "link",
            value: 1,
            target: 5,
            source: 8,
          },
          {
            type: "link",
            value: 1,
            target: 8,
            source: 9,
          },
          {
            type: "link",
            value: 1,
            target: 9,
            source: 10,
          },
          {
            type: "link",
            value: 1,
            target: 10,
            source: 11,
          },
          {
            type: "link",
            value: 1,
            target: 11,
            source: 12,
          },
          {
            type: "link",
            value: 1,
            target: 11,
            source: 13,
          },
        ],
      },
      gridSize: 100,
      selections: {},
      simulation: null,
      forceProperties: {
        center: {
          x: 0.5,
          y: 0.5,
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
circle.Frontend {
  r: 30;
  fill: #cce5ff;
  stroke: #003366;
}

circle.head {
  r: 30;
  fill: #c654cc;
}
circle.tail {
  r: 30;
  fill: #dfb2d7;
  stroke: #3b7fad;
}
circle.Stack {
  r: 25;
  fill: #ffe5e5;
  stroke: #660000;
}
circle.Backend {
  r: 30;
  fill: #b2e8b2;
  stroke: #001900;
}
circle.Algorithm {
  r: 30;
  fill: #e2d0f7;
  stroke: #003366;
}

circle.selected {
  stroke: #ff6666ff !important;
  stroke-width: 3px;
  animation: selected 2s infinite alternate ease-in-out;
}
circle.caption-circle {
  r: 10;
}
@keyframes selected {
  from {
    stroke-width: 5px;
    r: 26;
  }
  to {
    stroke-width: 1px;
    r: 30;
  }
}

.text {
  font: 10px sans-serif;
  pointer-events: none;
  text-shadow: 0 1px 0 #fff, 1px 0 0 #fff, 0 -1px 0 #fff, -1px 0 0 #fff;
}

rect.caption {
  fill: #ccccccac;
  stroke: #666;
  stroke-width: 1px;
}
.text.caption {
  font-size: 14px;
  font-weight: bol;
}
</style>
