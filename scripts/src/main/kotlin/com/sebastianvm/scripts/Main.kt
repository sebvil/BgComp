package com.sebastianvm.scripts

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

class Main : CliktCommand(name = "bgcomp-scripts") {
    override fun help(context: Context): String = "Scripts for the bgcomp project"

    override fun run() = Unit
}

fun main(args: Array<String>) = Main().subcommands(GenerateModuleCommand()).main(args)
