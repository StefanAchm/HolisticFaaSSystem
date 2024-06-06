exports.handler = function iterator (event, context, callback) {
  let index = event.iterator.index
  let step = event.iterator.step
  let count = event.iterator.count
 
  index += step
  let toLog = []
  if(event.par != null && event.toLog != null){
    toLog = event.toLog
    toLog.push(event.par[1].calculateTimeToGate.minutesToGate)
  }
 
  callback(null, { iterator:{
    index,
    step,
    count,
    continue: index <= count 
  },
  "toLog" : toLog
  })
}