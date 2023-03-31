<template>
  <div>
    <div class="navigation">
      <div class="menu">
        <ul>
          <li v-for="(row, index) in types"
              @click="setActive(index)"
              :class="{'active':row.active}">
            <a href="#">
              <font-awesome-icon class="icon" :icon="row.icon"/>
              <span class="title">{{ row.title }}</span>
            </a>
          </li>
          <div :class="{'indicator':true, 'on':(activeIndex>=0)}"
               :style="'transform: translateX(calc('+(70 * activeIndex) + 'px))'"></div>
        </ul>
      </div>
      <div class="section">
        <canvas ref="canvas"></canvas>
        <div class="tool" @click="togglePlay()">
          <font-awesome-icon v-if="!play" class="icon" :icon="['fa', 'play']"/>
          <font-awesome-icon v-if="play" class="icon" :icon="['fa', 'stop']"/>
        </div>
      </div>
    </div>
    {{ connected ? 'Connected' : 'Disconnected' }}
  </div>
</template>

<script>
export default {
  data() {
    return {
      data: {
        list: []
      },
      play: false,
      dropped: false,
      connected: false,
      activeIndex: -1,
      types: [
        {title: "Bubble Sort", icon: ['fas', 'ellipsis'], sortType: "BUBBLE_SORT", active: false},
        {title: "Quick Sort", icon: ['fas', 'jet-fighter'], sortType: "QUICK_SORT", active: false},
      ]
    }
  },
  mounted() {
    this.socket = new WebSocket("ws://localhost:8080/websocket")
    this.socket.onopen = () => {
      this.connected = true
    }
    this.socket.onclose = () => {
      this.connected = false
    }
    this.socket.onmessage = (msg) => {
      let obj = JSON.parse(msg.data)
      if (obj) {
        this.updateData(obj)
      }
    }
    this.defineCanvas()
  },

  methods: {
    togglePlay() {
      this.play = !this.play
      this.dropped = !this.play
      this.clearCanvas()
      this.pushSocket()
    },
    clearCanvas() {
      if(this.canvas){
        this.context.clearRect(0,0, 450, 500)
      }
    },
    defineCanvas() {
      this.canvas = this.$refs.canvas
      this.canvas.width = 450
      this.canvas.height = 500
      this.context = this.canvas.getContext("2d")
    },
    updateData(obj) {
      this.data = obj
      this.draw()

    },
    setActive(ind) {
      this.types.forEach((row, i) => {
        row.active = (ind === i)
      })
      this.activeIndex = ind
      this.play = false
      this.dropped = true
      this.pushSocket()
      this.clearCanvas()
    },
    pushSocket() {
      if (this.activeIndex >= 0) {
        let obj = this.types[this.activeIndex]
        obj.play = this.play
        obj.dropped = this.dropped
        this.socket.send(JSON.stringify(obj))
      }
    },
    draw(){
      let xStep = 0
      let yStep = 0
      if(this.data && this.activeIndex >= 0){
        this.clearCanvas()
        this.data.list.forEach((line, ind) => {
          if(yStep === 0){
            yStep = this.canvas.height - this.canvas.height / 800
            xStep = this.canvas.width / this.data.list.length
          }
          let x = (xStep * ind)
          let y = line.val
          this.drawLine(x, y, xStep - 1, yStep, this.data.index === ind ? 'yellow':'grey')
        })
      }
    },
    drawLine(x, y, w, h, fill){
      this.context.beginPath()
      this.context.rect(x, y, w, h)
      this.context.fillStyle = fill
      this.context.fill()
      this.context.lineWidth = 0
      this.context.strokeStyle = 'black'
    }
  },
  comments: {}
}
</script>
