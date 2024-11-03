package com.xavierclavel.services

import common.infodto.RecipeInfo
import org.koin.core.component.KoinComponent

class ExportService: KoinComponent {

    fun generateHTMLContent(recipes: List<RecipeInfo>) {

    }

    fun generateHTMLContent(recipe: RecipeInfo) {
        """
            ""${'"'}
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Your Simple HTML Page</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f0f0f0;
                }
                .container {
                    max-width: 800px;
                    margin: 0 auto;
                    padding: 20px;
                    background-color: #ffffff;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                }
                h1 {
                    color: #333333;
                }
                p {
                    color: #666666;
                    line-height: 1.6;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>Generate A PDF File</h1>
            </div>
        </body>
        </html>
    ""${'"'}
        """.trimIndent()
    }


}