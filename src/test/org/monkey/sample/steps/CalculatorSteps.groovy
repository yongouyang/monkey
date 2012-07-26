package org.monkey.sample.steps

import org.monkey.sample.Calculator

this.metaClass.mixin(cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(cucumber.runtime.groovy.EN)

Before() {
    calc = new Calculator()
}

Before("@stupid") {
    println "You are a stupid guy!"
}

Before("@notused") {
//    throw new RuntimeException("Never happens")
}

Before("@notused,@important", "@alsonotused") {
//    throw new RuntimeException("Never happens")
}

Given(~"I have entered (\\d+) into (.*) calculator") { int number, String ignore ->
    calc.push number
}

Given(~"(\\d+) into the") {->
    throw new RuntimeException("should never get here since we're running with --guess")
}

When(~"I press (\\w+)") { String opname ->
    result = calc."$opname"()
}

Then(~"the stored result should be (.*)") { double expected ->
    assert expected == result
}

Given(~'I have (\\d+) dream$') { int arg1 ->
    // Express the Regexp above with the code you wish you had

}

When(~'^RobotCat is happy$') { ->
    // Express the Regexp above with the code you wish you had

}

Then(~'^I am happy$') { ->
    // Express the Regexp above with the code you wish you had
}




